package app

import org.springframework.web.bind.annotation.{ RequestMapping, RequestParam, RestController }
import java.util.concurrent.atomic.AtomicLong

@RestController
class GreetingController {
  val counter = new AtomicLong()

  @RequestMapping(Array("/greeting"))
  def greeting(@RequestParam(value = "name", defaultValue = "World") name: String) =
    Greeting(counter.incrementAndGet(), s"Hello, ${name}")
}
