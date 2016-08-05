package app
import models._
import db._
import java.math.BigInteger
import javax.persistence.EntityManager
import javax.transaction.Transactional
import org.slf4j.{ Logger, LoggerFactory }
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.{ CommandLineRunner, SpringApplication }
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import scala.annotation.meta.getter

@SpringBootApplication
class Application {

  val log = LoggerFactory.getLogger("APPLICATION")

  @Bean
  def demo(em: EntityManager,
           cr: CustomerRepository
  ) = new CommandLineRunner {
    def run(args: String*) {
      val count = em.createNativeQuery("select count(*) from customers")
        .getSingleResult()
        .asInstanceOf[BigInteger]

      log.info(s"Records before initialization: ${count}")

      var c = cr.save(Customer(null, "Mike Wazovsky"))
      log.info(s"Inserted id = ${c.getId}")

      c = cr.findOne(c.getId)
      if(c != null)
        log.info(s"Found ${c.getName}")
    }
  }

}

object Application extends App {

  SpringApplication.run(classOf[Application])

}
