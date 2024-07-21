package flogger
import cats.effect.IO

trait IOLogging {
	protected implicit val logOutput: Log[IO] = getLogger(getClass)
}

