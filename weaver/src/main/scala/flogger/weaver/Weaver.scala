package flogger

import weaver.{Log => WeaverLog}
import weaver.SourceLocation
import cats.effect.IO
import flogger.*
import flogger.api.Level
import scala.jdk.CollectionConverters.*
import java.util as ju

class WeaverLogOutput(wl: WeaverLog[IO], implicitOutput: LogOutput[IO]) extends LogOutput[IO] {
	override val name: String = implicitOutput.name

	override def isEnabledFor(level: Level): IO[Boolean] = IO.pure(true)

	override def log(level: Level, jctx: ju.Map[String, String], msg: => String): IO[Unit] =
		log(level, jctx, msg, null)

	override def log(level: Level, jctx: ju.Map[String, String], msg: => String, error: Throwable): IO[Unit] = {
		// construct a fake source location that's slightly better than everything coming from this file
		implicit val sl: SourceLocation = SourceLocation(
			filePath = implicitOutput.name,
			fileRelativePath = implicitOutput.name,
			line = 0,
		)
		val ctx = jctx.asScala.toMap
		if (error == null) {
			level match {
				case Level.Error => wl.error(msg, ctx)
				case Level.Warn => wl.warn(msg, ctx)
				case Level.Info => wl.info(msg, ctx)
				case Level.Debug => wl.debug(msg, ctx)
				case Level.Trace => wl.debug(msg, ctx)
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
}

class WeaverLogCtx(wl: WeaverLog[IO], val context: ju.Map[String, String]) extends Log[IO] {

	override def unsafeContext: ju.Map[String, String] = context

	override def output(implicit implicitOutput: LogOutput[IO]): LogOutput[IO] = {
		new WeaverLogOutput(wl, implicitOutput)
	}

	override def addContext(ctx: (String, String)*): Log[IO] = new WeaverLogCtx(wl, Log.Impl.addContext(context, ctx))
}

// Note: intended to be used at the root level, so there should be no context requiring propagation
implicit def weaverLogCtx(implicit wl: WeaverLog[IO]): Log[IO] =
	new WeaverLogCtx(wl, Log.emptyCtx)
