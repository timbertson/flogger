package flogger.syntax

import flogger.*
import flogger.api.*

// free functions for logging, given implicit LogOutput and Log
def logEnabledFor[F[_]](level: Level)(implicit output: LogOutput[F]): F[Boolean] =
	output.isEnabledFor(level)

def error[F[_]](msg: => String, error: Throwable)(implicit log: Log[F], output: LogOutput[F]): F[Unit] =
	log.output.log(Level.Error, log.unsafeContext, msg, error)
def error[F[_]](msg: => String)(implicit log: Log[F], output: LogOutput[F]): F[Unit] =
	log.output.log(Level.Error, log.unsafeContext, msg)

def warn[F[_]](msg: => String, error: Throwable)(implicit log: Log[F], output: LogOutput[F]): F[Unit] =
	log.output.log(Level.Warn, log.unsafeContext, msg, error)
def warn[F[_]](msg: => String)(implicit log: Log[F], output: LogOutput[F]): F[Unit] =
	log.output.log(Level.Warn, log.unsafeContext, msg)

def info[F[_]](msg: => String, error: Throwable)(implicit log: Log[F], output: LogOutput[F]): F[Unit] =
	log.output.log(Level.Info, log.unsafeContext, msg, error)
def info[F[_]](msg: => String)(implicit log: Log[F], output: LogOutput[F]): F[Unit] =
	log.output.log(Level.Info, log.unsafeContext, msg)

def debug[F[_]](msg: => String, error: Throwable)(implicit log: Log[F], output: LogOutput[F]): F[Unit] =
	log.output.log(Level.Debug, log.unsafeContext, msg, error)
def debug[F[_]](msg: => String)(implicit log: Log[F], output: LogOutput[F]): F[Unit] =
	log.output.log(Level.Debug, log.unsafeContext, msg)

def trace[F[_]](msg: => String, error: Throwable)(implicit log: Log[F], output: LogOutput[F]): F[Unit] =
	log.output.log(Level.Trace, log.unsafeContext, msg, error)
def trace[F[_]](msg: => String)(implicit log: Log[F], output: LogOutput[F]): F[Unit] =
	log.output.log(Level.Trace, log.unsafeContext, msg)

