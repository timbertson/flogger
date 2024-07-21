package flogger.sync

import cats.effect.SyncIO
import flogger.*
import flogger.api.*

trait SyncLogging {
	protected implicit val logOutput: Log[SyncIO] = getLogger(getClass)
}

// shorthand for executing logs synchronously, given LogOutput[SyncIO] and Log[SyncIO]
def logEnabledFor(level: Level)(implicit output: LogOutput[SyncIO]): Boolean = {
	output.isEnabledFor(level).unsafeRunSync()
}

def error(msg: => String, error: Throwable)(implicit log: Log[SyncIO], output: LogOutput[SyncIO]): Unit =
	log.log(Level.Error, msg, error).unsafeRunSync()
def error(msg: => String)(implicit log: Log[SyncIO], output: LogOutput[SyncIO]): Unit =
	log.log(Level.Error, msg).unsafeRunSync()

def warn(msg: => String, error: Throwable)(implicit log: Log[SyncIO], output: LogOutput[SyncIO]): Unit =
	log.log(Level.Warn, msg, error).unsafeRunSync()
def warn(msg: => String)(implicit log: Log[SyncIO], output: LogOutput[SyncIO]): Unit =
	log.log(Level.Warn, msg).unsafeRunSync()

def info(msg: => String, error: Throwable)(implicit log: Log[SyncIO], output: LogOutput[SyncIO]): Unit =
	log.log(Level.Info, msg, error).unsafeRunSync()
def info(msg: => String)(implicit log: Log[SyncIO], output: LogOutput[SyncIO]): Unit =
	log.log(Level.Info, msg).unsafeRunSync()

def debug(msg: => String, error: Throwable)(implicit log: Log[SyncIO], output: LogOutput[SyncIO]): Unit =
	log.log(Level.Debug, msg, error).unsafeRunSync()
def debug(msg: => String)(implicit log: Log[SyncIO], output: LogOutput[SyncIO]): Unit =
	log.log(Level.Debug, msg).unsafeRunSync()

def trace(msg: => String, error: Throwable)(implicit log: Log[SyncIO], output: LogOutput[SyncIO]): Unit =
	log.log(Level.Trace, msg, error).unsafeRunSync()
def trace(msg: => String)(implicit log: Log[SyncIO], output: LogOutput[SyncIO]): Unit =
	log.log(Level.Trace, msg).unsafeRunSync()


trait SyncLogging {
	protected implicit val logOutput: Log[SyncIO] = getLogger(getClass)
}
