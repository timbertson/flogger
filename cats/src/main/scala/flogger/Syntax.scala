// package flogger

// import cats.effect.SyncIO
// import flogger.api.Level
// import java.{util => ju}
// import cats.effect.kernel.Sync

// // hoist a LogOutput[SyncIO] into another Sync effect
// extension (impl: LogOutput[SyncIO]) {
// 	def to[F[_]: Sync]: LogOutput[F] = new LogOutput[F] {
// 		override val name: String = impl.name

// 		override def log(level: Level, ctx: ju.Map[String, String], msg: => String): F[Unit] =
// 			impl.log(level, ctx, msg).to[F]

// 		override def log(level: Level, ctx: ju.Map[String, String], msg: => String, error: Throwable): F[Unit] =
// 			impl.log(level, ctx, msg, error).to[F]

// 		override def isEnabledFor(level: Level): F[Boolean] =
// 			impl.isEnabledFor(level).to[F]
// 	}
// }

// extension (s: Log[SyncIO]) {
// 	def to[F[_]](implicit output: LogOutput[F]): Log[F] = new Log[F] {
// 		override def log(level: Level, msg: => String)(implicit output: LogOutput[F]): F[Unit] =
// 			s.log(level, msg).to[F]

// 		override def log(level: Level, msg: => String, error: Throwable)(implicit output: LogOutput[F]): F[Unit] =
// 			s.log(level, msg, error).to[F]

// 		override def addContext(ctx: (String, String)*): Log[F] = ???

// 	}
// }
