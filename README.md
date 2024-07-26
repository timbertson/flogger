<!-- <img src="http://gfxmonk.net/dist/status/project/flogger.png"> -->

![](/flogger.png)

`flogger` is a simple, functional `cats-effect` logger wrapper much like [log4cats](https://github.com/typelevel/log4cats) but with more ergonomic support for contextual logging. It doesn't try to reimplement logging itself, it uses the standard `slf4j` API under the hood, with contextual information populated in [MDC](https://logback.qos.ch/manual/mdc.html).

The primary difference is that it has separate notion of a `LogOutput` (the underlying logger identity) and `Log` (the object you interact with, which can include contextual information).

When using `log4cats` in projects, I always ended up needing to construct a wrapper which allowed me to pass around context separately from the actualy logger, because log4cats requires you to pass an explicit `ctx: Map[String, String]` at every log site, but my context was typically "whatever was passed in from the caller".

## Getting started

```scala
libraryDependencies += "net.gfxmonk" %% "flogger" % "VERSION" // check github tags for latest release
```

## Usage

Idiomatic usage looks like this:

```scala
import cats.effect.*
import flogger.*

// IOLogging provides an implicit Logger[IO]
object ExampleMain extends IOApp.Simple with IOLogging {
	override def run: IO[Unit] = {
		// set up an empty logger at the top level
		implicit val rootLog: Log[IO] = Log.empty
		someFunction()
	}

	def someFunction(implicit log: Log[IO]) = {
		for {
			_ <- log.info("Welcome!")
			_ <- List(1,2,3,4).traverse { item =>
				// To add context, you typically create an implicit Log[F] in a specific scope.
				// This can be used in multiple places to add extra details at different levels
				implicit val itemLogger: Log[IO] = log.addContext("item" -> item.toString())
				doItemConversion(item)
			}
		} yield ()
	}

	def doItemConversion(item: Int)(implicit log: Log[IO]): IO[String] = {
		for {
			_ <- log.warn(s"Converting ...")
		} yield (item * 2).toString()
	}
}
```

More detailed usage can be found in [src/test/scala/flogger/test/Example.scala]().

# Extras:

### Weaver-test integration

The `flogger-weaver` module adds a handy implicit conversion from a weaver `Logger` to a flogger `Log`, so output from a test will be captured. It's in the `flogger` namespace, so `import flogger.*` will bring it in scope. You need to make the weaver `Logger` implicit too, as in:

```scala
loggedTest("some functionality") { implicit log =>
	// you can call any function that accepts an implicit `flogger.Log[F]`
}
```

**Note:** if you import `flogger.*` and `weaver.*`, you'll get an ambiguous import of `Log` from both packages. You can either import just what you need, or add an additional `import Flogger.log` after the wildcard imports to clarify which one you want.

### Core

If you don't want to depend on cats-effect, `flogger-core` only depends on `cats-effect-kernel` (for typeclasses).

### Sync logging

Sometimes you just want to log something, without all the monads. If you import `flogger.sync.*` (with the main `flogger` package), it'll populate top level functions `info`, `warn`, etc. These require an implicit `Log[SyncIO]` and `LogOutput[SyncIO]` (you can extend the `SyncLogging` trait for this), and perform synchronous logging.

There's a `Log.from(otherLog)` method which creates a logger in a different `F[_]` using the provided log's context, which is handy if you want to do some localized sync logging using some context passed in as a `Log[IO]`.

This probably isn't worth it if you want to do synchronous logging throughout your app, unless you really love the contextual logging support. But it can be really useful for a targeted method or class which you don't want to (or can't) lift into `IO`.
