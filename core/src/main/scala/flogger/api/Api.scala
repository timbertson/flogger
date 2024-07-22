package flogger.api

import flogger.*
import org.slf4j
import java.{util => ju}
import cats.effect.kernel.Sync

// internal bits only typically imported for advanced usage

trait Level
object Level {
	case object Error extends Level
	case object Warn extends Level
	case object Info extends Level
	case object Debug extends Level
	case object Trace extends Level
}

class Slf4jOutput[F[_]](override val name: String, logger: slf4j.Logger)(implicit F: Sync[F]) extends LogOutput[F] {
	override def log(level: Level, ctx: ju.Map[String, String], msg: => String): F[Unit] =
		logImpl(level, ctx, msg, null)

	override def log(level: Level, ctx: ju.Map[String, String], msg: => String, error: Throwable): F[Unit] =
		logImpl(level, ctx, msg, error)

	private inline def logImpl(level: Level, ctx: ju.Map[String, String], msg: => String, error: Throwable /* nullable */): F[Unit] = {
		F.delay {
			if (isEnabledSync(level)) {
				var oldMDC: ju.Map[String, String] = null
				val useMdc = (!ctx.isEmpty())
				if (useMdc) {
					oldMDC = slf4j.MDC.getCopyOfContextMap()
					slf4j.MDC.setContextMap(ctx)
				}
				try {
					if (error == null) {
						level match {
							case Level.Error => logger.error(msg)
							case Level.Warn => logger.warn(msg)
							case Level.Info => logger.info(msg)
							case Level.Debug => logger.debug(msg)
							case Level.Trace => logger.trace(msg)
						}
					} else {
						level match {
							case Level.Error => logger.error(msg, error)
							case Level.Warn => logger.warn(msg, error)
							case Level.Info => logger.info(msg, error)
							case Level.Debug => logger.debug(msg, error)
							case Level.Trace => logger.trace(msg, error)
						}
					}
				} finally {
					if (useMdc) {
						if (oldMDC eq null) slf4j.MDC.clear()
						else slf4j.MDC.setContextMap(oldMDC)
					}
				}
			} else {
				()
			}
		}
	}

	private inline def isEnabledSync(level: Level): Boolean = level match {
		case Level.Error => logger.isErrorEnabled()
		case Level.Warn => logger.isWarnEnabled()
		case Level.Info => logger.isInfoEnabled()
		case Level.Debug => logger.isDebugEnabled()
		case Level.Trace => logger.isTraceEnabled()
	}

	override def isEnabledFor(level: Level): F[Boolean] = F.delay(isEnabledSync(level))
}

object Slf4jOutput {
	def apply[F[_]](name: String)(implicit F: Sync[F]): Slf4jOutput[F] =
		new Slf4jOutput[F](name, slf4j.LoggerFactory.getLogger(name))
}
