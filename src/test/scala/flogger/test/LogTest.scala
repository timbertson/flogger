package flogger.test

import weaver.*
import flogger.*
import flogger.Log
import cats.effect.kernel.Ref
import cats.effect.IO
import scala.collection.immutable.Queue
import flogger.api.Level
import scala.jdk.CollectionConverters.*
import java.{util => ju}

object LogOutputTest extends SimpleIOSuite {
	case class LogEntry(ctx: Map[String, String], msg: String)
	class LogCapture(ref: Ref[IO, Queue[LogEntry]]) extends LogOutput[IO] {
		def logs = ref.get.map(_.toList)

		override val name: String = "captured"

		override def log(level: Level, ctx: ju.Map[String, String], msg: => String): IO[Unit] =
			ref.update(_.appended(LogEntry(ctx.asScala.toMap, msg)))

		override def log(level: Level, ctx: ju.Map[String, String], msg: => String, error: Throwable): IO[Unit] =
			log(level, ctx, msg)

		override def isEnabledFor(level: Level): IO[Boolean] = ???

	}

	implicit val log: IOLog = Log.empty

	object LogCapture {
		def apply(): LogCapture = new LogCapture(Ref.unsafe(Queue.empty))
	}

	test("Log entries") {
		implicit val capture: LogCapture = LogCapture()
		for {
			_ <- LogTestObject.doSomeLogging()
			_ <- {
				implicit val subLogger: Log[IO] = log.addContext("testCase" -> "log entries")
				LogTestObject.doSomeLogging *>
				subLogger.withContext("block" -> "true") { implicit log =>
					LogTestObject.doSomeLogging
				}
			}
			logs <- capture.logs
		} yield {
			expect(logs == List(
				LogEntry(Map(), "msg"),
				LogEntry(Map("testCase" -> "log entries"), "msg"),
				LogEntry(Map("testCase" -> "log entries", "block" -> "true"), "msg"),
			))
		}
	}
	
	pureTest("logger name") {
		expect(NameTest.output.name == "flogger.test.LogOutputTest$NameTest$")
	}

	object NameTest extends IOLogging {
		def output = implicitly[LogOutput[IO]]
	}
}

object LogTestObject {
	def doSomeLogging(implicit log: Log[IO], output: LogOutput[IO]) = {
		log.info("msg")
	}
}

