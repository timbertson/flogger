package flogger.sync

import cats.effect.SyncIO
import flogger.*
import flogger.api.*

// shorthand for executing logs synchronously, given LogOutput[SyncIO] and Log[SyncIO]
def logEnabledFor(level: Level)(implicit output: LogOutput[SyncIO]): Boolean =
	output.isEnabledFor(level).unsafeRunSync()

def error(msg: => String, error: Throwable)(implicit log: Log[SyncIO], output: LogOutput[SyncIO]): Unit =
	log.output.log(Level.Error, log.unsafeContext, msg, error).unsafeRunSync()
def error(msg: => String)(implicit log: Log[SyncIO], output: LogOutput[SyncIO]): Unit =
	log.output.log(Level.Error, log.unsafeContext, msg).unsafeRunSync()

def warn(msg: => String, error: Throwable)(implicit log: Log[SyncIO], output: LogOutput[SyncIO]): Unit =
	log.output.log(Level.Warn, log.unsafeContext, msg, error).unsafeRunSync()
def warn(msg: => String)(implicit log: Log[SyncIO], output: LogOutput[SyncIO]): Unit =
	log.output.log(Level.Warn, log.unsafeContext, msg).unsafeRunSync()

def info(msg: => String, error: Throwable)(implicit log: Log[SyncIO], output: LogOutput[SyncIO]): Unit =
	log.output.log(Level.Info, log.unsafeContext, msg, error).unsafeRunSync()
def info(msg: => String)(implicit log: Log[SyncIO], output: LogOutput[SyncIO]): Unit =
	log.output.log(Level.Info, log.unsafeContext, msg).unsafeRunSync()

def debug(msg: => String, error: Throwable)(implicit log: Log[SyncIO], output: LogOutput[SyncIO]): Unit =
	log.output.log(Level.Debug, log.unsafeContext, msg, error).unsafeRunSync()
def debug(msg: => String)(implicit log: Log[SyncIO], output: LogOutput[SyncIO]): Unit =
	log.output.log(Level.Debug, log.unsafeContext, msg).unsafeRunSync()

def trace(msg: => String, error: Throwable)(implicit log: Log[SyncIO], output: LogOutput[SyncIO]): Unit =
	log.output.log(Level.Trace, log.unsafeContext, msg, error).unsafeRunSync()
def trace(msg: => String)(implicit log: Log[SyncIO], output: LogOutput[SyncIO]): Unit =
	log.output.log(Level.Trace, log.unsafeContext, msg).unsafeRunSync()
