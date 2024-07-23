package flogger
import cats.effect.IO
import cats.effect.SyncIO

type IOLog = Log[IO]

trait IOLogging {
	protected implicit val logOutputIO: LogOutput[IO] = getLogger[IO](this.getClass)
}

trait SyncLogging {
	protected implicit val logOutputSync: LogOutput[SyncIO] = getLogger(this.getClass)
}
