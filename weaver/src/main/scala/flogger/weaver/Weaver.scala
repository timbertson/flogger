package flogger

import weaver.{Log => WeaverLog}
import weaver.SourceLocation
import cats.effect.IO
import flogger.*
import flogger.api.Level

class WeaverLogCtx(wl: WeaverLog[IO], ctx: Map[String, String]) extends Log[IO] {
	override def log(level: Level, msg: => String)(implicit output: LogOutput[IO]): IO[Unit] = {
		log(level, msg, null)
	}

	override def log(level: Level, msg: => String, error: Throwable)(implicit output: LogOutput[IO]): IO[Unit] = {
		// construct a fake source location that's slightly better than everything coming from this file
		implicit val sl: SourceLocation = SourceLocation(
			filePath = output.name,
			fileRelativePath = output.name,
			line = 0,
		)
		if (error == null) {
			level match {
				case Level.Error => wl.error(msg)
				case Level.Warn => wl.warn(msg)
				case Level.Info => wl.info(msg)
				case Level.Debug => wl.debug(msg)
				case Level.Trace => wl.debug(msg)
			}
		} else {
			level match {
				case Level.Error => wl.error(msg, ctx, error)
				case Level.Warn => wl.warn(msg, ctx, error)
				case Level.Info => wl.info(msg, ctx, error)
				case Level.Debug => wl.debug(msg, ctx, error)
				case Level.Trace => wl.debug(msg, ctx, error)
			}
		}
	}

	override def addContext(ctx: (String, String)*): Log[IO] = new WeaverLogCtx(wl, this.ctx ++ ctx)
}

// Note: intended to be used at the root level, so there should be no context requiring propagation
implicit def weaverLogCtx(implicit wl: WeaverLog[IO]): Log[IO] =
	new WeaverLogCtx(wl, Map.empty)
