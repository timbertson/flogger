package bench
import flogger.*
import cats.effect.*
import cats.syntax.all.*

import org.openjdk.jmh.annotations.*
import org.slf4j
import scala.jdk.CollectionConverters.*
import scala.annotation.tailrec
import org.typelevel.log4cats

@State(Scope.Benchmark)
@BenchmarkMode(Array(Mode.AverageTime))
class LoggerBench extends IOLogging with SyncLogging {
	import cats.effect.unsafe.implicits.global
	implicit val loggerName: log4cats.LoggerName = log4cats.LoggerName("logger")

	val size = 1_0000
	val jlogger = slf4j.LoggerFactory.getLogger("logger")
	val ctxMap = Map("tag1" -> "value1", "tag2" -> "value2", "tag3" -> "value3")

	@Benchmark def b01_logSlf4j: Unit = {
		@tailrec
		def logRec(n: Int): Unit = {
			if (n <= 0) {
				()
			} else {
				jlogger.info("test")
				logRec(n-1)
			}
		}

		logRec(size)
	}

	@Benchmark def b02_slf4jWithMDC: Unit = {
		slf4j.MDC.setContextMap(ctxMap.asJava)
		b01_logSlf4j
	}


	@Benchmark def b03_logSlf4jInCats: Unit = {
		def logRec(n: Int): IO[Unit] = {
			if (n <= 0) {
				IO.unit
			} else {
				IO.pure(jlogger.info("test")).flatMap(_ => logRec(n-1))
			}
		}
		logRec(size).unsafeRunSync()
	}

	@Benchmark def b05_log4CatsBench: Unit = {
		import org.typelevel.log4cats.*
		import org.typelevel.log4cats
		import org.typelevel.log4cats.slf4j.*
		val logger: log4cats.Logger[IO] = Slf4jLogger.getLogger

		def logRec(n: Int): IO[Unit] = {
			if (n <= 0) {
				IO.unit
			} else {
				logger.info("test").flatMap(_ => logRec(n-1))
			}
		}
		logRec(size).unsafeRunSync()
	}

	@Benchmark def b06_log4CatsCtx: Unit = {
		import org.typelevel.log4cats.*
		import org.typelevel.log4cats.slf4j.*
		val logger: SelfAwareStructuredLogger[IO] = Slf4jFactory.create[IO].getLogger

		def logRec(n: Int): IO[Unit] = {
			if (n <= 0) {
				IO.unit
			} else {
				logger.info(ctxMap)("test").flatMap(_ => logRec(n-1))
			}
		}
		logRec(size).unsafeRunSync()
	}

	@Benchmark def b07_logFlogger: Unit = {
		import FloggerImpl.floggerRec
		implicit val log: Log[IO] = Log.empty
		floggerRec(size).unsafeRunSync()
	}

	@Benchmark def b08_logFloggerSync: Unit = {
		import FloggerImpl.floggerRec
		implicit val log: Log[SyncIO] = Log.empty
		floggerRec[SyncIO](size).unsafeRunSync()
	}

	@Benchmark def b09_logFloggerWithCtx: Unit = {
		import FloggerImpl.floggerRec
		implicit val log: Log[IO] = Log.empty.addContext("tag1" -> "value1", "tag2" -> "value2", "tag3" -> "value3")
		floggerRec(size).unsafeRunSync()
	}

	@Benchmark def b10_logFloggerWithCtxPerLog: Unit = {
		def logRec(n: Int)(implicit log: Log[IO]): IO[Unit] = {
			if (n <= 0) {
				IO.unit
			} else {
				log.withContext("tag1" -> "value1", "tag2" -> "value2", "tag3" -> "value3")(_.info("test"))
					.flatMap(_ => logRec(n-1))
			}
		}
		implicit val log: Log[IO] = Log.empty
		logRec(size).unsafeRunSync()
	}

	object FloggerImpl {
		def floggerRec[F[_]](n: Int)(implicit log: Log[F], logOutput: LogOutput[F], F: Sync[F]): F[Unit] = {
			if (n <= 0) {
				F.unit
			} else {
				log.info("test").flatMap(_ => floggerRec(n-1))
			}
		}
	}

}

