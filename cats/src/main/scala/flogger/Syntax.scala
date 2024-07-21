package flogger

import cats.effect.SyncIO
import flogger.api.*

// hoist a LogOutput[SyncIO] into another Sync effect
extension (s: LogOutput[SyncIO]) {
	def to[F[_]]: LogOutput[F] = new LogOutput[F] {
	}
}

// hoist a LogOutput[SyncIO] into another Sync effect
extension (s: LogOutput[SyncIO]) {
	def to[F[_]]: LogOutput[F] = new LogOutput[F] {
	}
}
