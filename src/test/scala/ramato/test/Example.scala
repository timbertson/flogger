package flogger.test

import weaver.IOSuite
import flogger.*
import cats.effect.IO

object ExampleTest extends IOSuite {
	test("sample usage") {
		implicit val output: LogOutput[IO] = getLogger[IO]("ExampleTest")
		IO.pure(failure("TODO"))
	}
}

object SampleObject extends IOLogging {
	def doSomeLogging(implicit log: Log[IO]) = {
		log.info("Here I am!")
	}
}
