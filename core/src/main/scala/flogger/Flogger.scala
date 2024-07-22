package flogger

import flogger.api.*

import java.util.Map as JavaMap
import java.util.HashMap
import scala.reflect.ClassTag
import cats.effect.kernel.Sync
import scala.jdk.CollectionConverters.*

// Log output is a place to send logs. Typically there's an implicit one
// per class, with a logger name matching the class.
// You will mostly interact with the `LogCtx` API instead
trait LogOutput[F[_]] {
	val name: String
	def log(level: Level, ctx: JavaMap[String, String], msg: => String, error: Throwable): F[Unit]
	def log(level: Level, ctx: JavaMap[String, String], msg: => String): F[Unit]
	def isEnabledFor(level: Level): F[Boolean]
}

// construct slf4j loggers
def getLogger[F[_]](name: String)(implicit F: Sync[F]): LogOutput[F] = Slf4jOutput[F](name)
def getLogger[F[_]](cls: Class[?])(implicit F: Sync[F]): LogOutput[F] = getLogger[F](cls.getName)
def getLogger[F[_], Cls](implicit ct: ClassTag[Cls], F: Sync[F]): LogOutput[F] = getLogger[F](ct.getClass().getName())

// Log is the main trait. It manages context and provides the logging API, although
// the heavy lifting is done by the implicit LogOutput
trait Log[F[_]] {
	// overrideable hook for intercepting logs
	def output(implicit implicitOutput: LogOutput[F]): LogOutput[F] = implicitOutput

	// unsafe because this is mutable, but you should never mutate it
	def unsafeContext: java.util.Map[String, String]
	def addContext(ctx: (String, String)*): Log[F]

	// -- user API with trivial implementations --
	def isEnabledFor(level: Level)(implicit output: LogOutput[F]): F[Boolean] = {
		output.isEnabledFor(level)
	}
	
	def withContext[R](ctx: (String, String)*)(block: Log[F] => F[R]): F[R] = {
		block(this.addContext(ctx*))
	}
	
	def error(msg: => String, error: Throwable)(implicit implicitOutput: LogOutput[F]): F[Unit] = this.output.log(Level.Error, unsafeContext, msg, error)
	def error(msg: => String)(implicit implicitOutput: LogOutput[F]): F[Unit] = this.output.log(Level.Error, unsafeContext, msg)

	def warn(msg: => String, error: Throwable)(implicit implicitOutput: LogOutput[F]): F[Unit] = this.output.log(Level.Warn, unsafeContext, msg, error)
	def warn(msg: => String)(implicit implicitOutput: LogOutput[F]): F[Unit] = this.output.log(Level.Warn, unsafeContext, msg)

	def info(msg: => String, error: Throwable)(implicit implicitOutput: LogOutput[F]): F[Unit] = this.output.log(Level.Info, unsafeContext, msg, error)
	def info(msg: => String)(implicit implicitOutput: LogOutput[F]): F[Unit] = this.output.log(Level.Info, unsafeContext, msg)

	def debug(msg: => String, error: Throwable)(implicit implicitOutput: LogOutput[F]): F[Unit] = this.output.log(Level.Debug, unsafeContext, msg, error)
	def debug(msg: => String)(implicit implicitOutput: LogOutput[F]): F[Unit] = this.output.log(Level.Debug, unsafeContext, msg)

	def trace(msg: => String, error: Throwable)(implicit implicitOutput: LogOutput[F]): F[Unit] = this.output.log(Level.Trace, unsafeContext, msg, error)
	def trace(msg: => String)(implicit implicitOutput: LogOutput[F]): F[Unit] = this.output.log(Level.Trace, unsafeContext, msg)
}

object Log {
	private [flogger] val emptyCtx = new HashMap[String, String](0)

	def empty[F[_]]: Log[F] = new Impl[F](emptyCtx)
	
	class Impl[F[_]](ctx: JavaMap[String, String]) extends Log[F] {
		override def addContext(ctx: (String, String)*): Log[F] = {
			new Impl[F](Impl.addContext(this.ctx, ctx))
		}
		
		override def unsafeContext: java.util.Map[String, String] = ctx
	}

	object Impl {
		def addContext(base: JavaMap[String, String], add: Seq[(String, String)]): JavaMap[String, String] = {
			if (add.isEmpty) {
				return base
			}
			val newCtx = new HashMap[String, String](base.size + add.size, 0.8)
			newCtx.putAll(base)
			add.foreach { (k, v) =>
				val _ = newCtx.put(k, v)
			}
			newCtx
		}
	}
}
