package flogger.test

import weaver.*
import flogger.*
import cats.effect.kernel.Ref
import cats.effect.IO
import scala.collection.immutable.Queue

object LogTest extends IOSuite {
	case class LogEntry(logger: String, ctx: Map[String, String], msg: String)
	class LogCapture(ref: Ref[IO, Queue[LogEntry]]) extends LogOutput[IO] {
	}

	object LogCapture {
		def apply() = Ref.unsafe
	}

	test("Logging trait") {
		implicit val output: LogOutput[IO] = getLogger()
	}
}

object SampleObject extends Logging {
	def doSomeLogging(implicit log: Log[IO]) = {
		log.info("Here I am!")
	}
}

