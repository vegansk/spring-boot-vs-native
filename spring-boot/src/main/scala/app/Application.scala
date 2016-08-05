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
           cr: CustomersRepository,
           or: OrdersRepository,
           pr: ProductsRepository
  ) = new CommandLineRunner {
    def run(args: String*) {
      val customers = 1 to 3 map(id => cr.save(Customer(null, s"Customer${id}")))
      val products = 1 to 10 map(id => pr.save(Product(null, s"Product${id}", if(id == 10) null else new java.math.BigDecimal(id))))
    }
  }

}

object Application extends App {

  SpringApplication.run(classOf[Application])

}
