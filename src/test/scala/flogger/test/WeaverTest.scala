// cheat; we want to access some internals
package weaver

// import weaver.*
import flogger.*
import flogger.Log
import cats.effect.IO
import cats.effect.kernel.Ref

object WeaverIntegrationTest extends SimpleIOSuite with IOLogging {
	test("weaver log integration") {
		// we build a weaver log (as would be given to a test), then
		// the implicit in flogger turns that into a Log[IO]
		val logRef = Ref.unsafe(List.empty[weaver.Log.Entry])
		implicit val wl: weaver.Log[IO] = weaver.Log.collected(ref = logRef, ts = IO.pure(0L))
		for {
			_ <- logSomeMessages
			_ <- {
				// need this in an outer block so it doesn't see the implicit subLogger
				val implicitLog = implicitly[Log[IO]]
				{
					implicit val subLogger: Log[IO] = implicitLog.addContext("testCase" -> "log entries")
					subLogger.withContext("block" -> "true") { implicit log =>
						logSomeMessages
					}
				}
			}
			logs <- logRef.get
		} yield {
			expect(logs.map(entry => (entry.msg, entry.ctx)) == List(
				("message 1", Map()),
				("message 2", Map()),
				("message 3", Map()),

				("message 1", Map("testCase" -> "log entries", "block" -> "true")),
				("message 2", Map("testCase" -> "log entries", "block" -> "true")),
				("message 3", Map("testCase" -> "log entries", "block" -> "true")),
			))
		}
	}
	
	def logSomeMessages(implicit log: Log[IO]) = {
		log.info("message 1") >>
		log.info("message 2") >>
		log.info("message 3")
	}
}
