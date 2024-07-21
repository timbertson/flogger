package flogger

import flogger.api.*

import java.util.Map as JavaMap
import java.util.HashMap
import scala.reflect.ClassTag
import cats.effect.kernel.Sync

// Log output is a place to send logs. It should not be used directly by user code, but
// typically there's an implicit one per class, with a logger name matching the class
trait LogOutput[F[_]] {
	val name: String
	def log(level: Level, ctx: JavaMap[String, String], msg: => String, error: Throwable): F[Unit]
	def log(level: Level, ctx: JavaMap[String, String], msg: => String): F[Unit]
	def isEnabledFor(level: Level): F[Boolean]
}

def getLogger[F[_]](name: String)(implicit F: Sync[F]): LogOutput[F] = Slf4jOutput[F](name)
def getLogger[F[_]](cls: Class[?])(implicit F: Sync[F]): LogOutput[F] = getLogger[F](cls.getName)
def getLogger[F[_], Cls](implicit ct: ClassTag[Cls], F: Sync[F]): LogOutput[F] = getLogger[F](ct.getClass().getName())

// trait Logging[F[_]] {
// 	protected implicit val logOutput: LogOutput[F] = getLogger(this.getClass())
// }

// Log is the core trait. It manages context and provides the logging API, although
// the heavy lifting is done by the implicit LogOutput
trait Log[F[_]] {
	// core required methods
	def log(level: Level, msg: => String, error: Throwable)(implicit output: LogOutput[F]): F[Unit]
	def log(level: Level, msg: => String)(implicit output: LogOutput[F]): F[Unit]
	
	def addContext(ctx: (String, String)*): Log[F]

	// user API with trivial implementationa
	def isEnabledFor(level: Level)(implicit output: LogOutput[F]): F[Boolean] = {
		output.isEnabledFor(level)
	}
	
	def withContext[R](ctx: (String, String)*)(block: Log[F] => F[R]): F[R] = {
		block(this.addContext(ctx*))
	}
	
	def error(msg: => String, error: Throwable)(implicit output: LogOutput[F]): F[Unit] = log(Level.Error, msg, error)
	def error(msg: => String)(implicit output: LogOutput[F]): F[Unit] = log(Level.Error, msg)

	def warn(msg: => String, error: Throwable)(implicit output: LogOutput[F]): F[Unit] = log(Level.Warn, msg, error)
	def warn(msg: => String)(implicit output: LogOutput[F]): F[Unit] = log(Level.Warn, msg)

	def info(msg: => String, error: Throwable)(implicit output: LogOutput[F]): F[Unit] = log(Level.Info, msg, error)
	def info(msg: => String)(implicit output: LogOutput[F]): F[Unit] = log(Level.Info, msg)

	def debug(msg: => String, error: Throwable)(implicit output: LogOutput[F]): F[Unit] = log(Level.Debug, msg, error)
	def debug(msg: => String)(implicit output: LogOutput[F]): F[Unit] = log(Level.Debug, msg)

	def trace(msg: => String, error: Throwable)(implicit output: LogOutput[F]): F[Unit] = log(Level.Trace, msg, error)
	def trace(msg: => String)(implicit output: LogOutput[F]): F[Unit] = log(Level.Trace, msg)
}

object Log {
	private val emptyCtx = new HashMap[String, String](0)

	def empty[F[_]]: Log[F] = new Impl[F](emptyCtx)
	
	class Impl[F[_]](ctx: JavaMap[String, String]) extends Log[F] {
		inline def log(level: Level, msg: => String)(implicit output: LogOutput[F]): F[Unit] = {
			output.log(level, ctx, msg)
		}
		
		inline def log(level: Level, msg: => String, error: Throwable)(implicit output: LogOutput[F]): F[Unit] = {
			output.log(level, ctx, msg, error)
		}

		def addContext(ctx: (String, String)*): Log[F] = {
			new Impl[F](Impl.addContext(this.ctx, ctx))
		}
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
