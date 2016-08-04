import java.util.concurrent.atomic.AtomicLong

object Service {
  private val counter = new AtomicLong()

  def greeting(name: String) =
    Greeting(counter.incrementAndGet(), s"Hello, ${name}")
}
