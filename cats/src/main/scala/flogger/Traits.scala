package flogger
import cats.effect.IO
import cats.effect.SyncIO

type IOLog = Log[IO]

trait IOLogging {
	protected implicit val logOutput: LogOutput[IO] = getLogger[IO](this.getClass)
}

trait SyncLogging {
	protected implicit val logOutput: LogOutput[SyncIO] = getLogger(this.getClass)
}
