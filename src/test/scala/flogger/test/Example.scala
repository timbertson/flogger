package flogger.test

import cats.syntax.all.*
import weaver.*
import flogger.*
import flogger.Log
import cats.effect.IO
import cats.effect.SyncIO
import cats.effect.IOApp

object ExampleMain extends IOApp.Simple with IOLogging {
	override def run: IO[Unit] = {
		implicit val rootLog: Log[IO] = Log.empty
		toplevelFunction()
	}

	def toplevelFunction(implicit log: Log[IO]) = {
		for {
			_ <- log.info("Welcome!")
			_ <- List(1,2,3,4).traverse { item =>
				// To add context, you typically create an implicit Log[F] in a specific scope.
				// This can be used in multiple places to add extra details at different levels
				implicit val itemLogger: Log[IO] = log.addContext("item" -> item.toString())
				doItemConversion(item)
			}
			_ <- freeFunctionSyntax
			_ <- ioAliases
			_ <- log.withContext("tag" -> "one-off") { implicit log =>
				describeMDC
			}
			_ <- DifferentObject.shareContext
		} yield ()
	}
	
	def doItemConversion(item: Int)(implicit log: Log[IO]): IO[String] = {
		log.warn(s"Converting ...") >>
		IO.pure((item * 2).toString())
	}
	
	def freeFunctionSyntax(implicit log: Log[IO]): IO[Unit] = {
		import flogger.syntax.*
		info("toplevel functions are provided, requiring an implicit Log and LogOutput")
	}
	
	def ioAliases(implicit log: IOLog): IO[Unit] = {
		log.info(
			"IOLog is a convenience alias for Log[IO], while IOLogging is an IO specific trait " +
			"providing an implicit `LogOutput[IO]`")
	}
	
	def describeMDC(implicit log: Log[IO]): IO[Unit] = {
		log.info("context key/values are used to populate MDC for the SLF4J backend")
	}
}

object DifferentObject extends IOLogging {
	def shareContext(implicit log: Log[IO]) = {
		log.info(
			"logging context is usually inherited across functions using implicit arguments, " +
			"but the underlying logger name comes from LogOutput, which is " +
			"generally specific to a class."
		)
	}
}

object SyncExample extends SyncLogging {
	import flogger.sync.*

	def syncLog(implicit log: Log[SyncIO]) = {
		info(
			"For some uses cases you may need synchronous logging using the same building blocks. " +
			"You can use SyncIO and the convenience functions in flogger.sync.* to make this easy."
		)
	}
}

object ExampleTest extends SimpleIOSuite with IOLogging {
	// run the example as a test (no real assertions though)
	test("sample usage") {
		ExampleMain.run.map(succeed)
	}
	
}
