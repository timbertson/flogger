package flogger
import _root_.cats.effect.IO
import _root_.cats.effect.SyncIO

type IOLog = Log[IO]

trait IOLogging {
	protected implicit val logOutputIO: LogOutput[IO] = LogOutput[IO](this.getClass)
}

trait SyncLogging {
	protected implicit val logOutputSync: LogOutput[SyncIO] = LogOutput[SyncIO](this.getClass)
}
