package flogger.test

import weaver.*
import flogger.*
import flogger.Log
import cats.effect.*
import flogger.sync.*

object SyncTest extends FunSuite {
	// make sure an expression produces Unit, not e.g. IO[Unit]
	private def unit(value: Unit) = ()

	test("sync logging syntax") {
		implicit val logOutput: LogOutput[SyncIO] = LogOutput[SyncIO]("test")
		implicit val rootCtx: Log[SyncIO] = Log.empty
		unit(info("sync"))
		success
	}

	test("conversion from Log[?]") {
		// we can't convert a SyncIO logger, but we can adapt a Log's context.
		// But that's not too annoying, you can extend both IOLogging and SyncLogging
		implicit val logOutput: LogOutput[SyncIO] = LogOutput[SyncIO]("test")
		implicit val ioCtx: Log[IO] = Log.empty
		implicit val ctx: Log[SyncIO] = Log.from(implicitly[Log[IO]])
		unit(info("sync"))
		success
	}
}
