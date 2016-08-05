package app
import java.sql.Timestamp
import java.time.LocalDateTime
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
import org.springframework.data.domain.PageRequest
import scala.annotation.meta.getter
import scala.collection.JavaConverters._

@SpringBootApplication
class Application {

  val log = LoggerFactory.getLogger("APPLICATION")

  @Bean
  def demo(em: EntityManager,
           cr: CustomersRepository,
           or: OrdersRepository,
           pr: ProductsRepository,
           opr: OrderProductsRepository
  ) = new CommandLineRunner {
    def run(args: String*) {

      def toTimestamp(ldt: LocalDateTime): Timestamp = Timestamp.valueOf(ldt)
      def toBigDecimal(v: Double) = new java.math.BigDecimal(v)

      val p1 = pr.save(Product(null, "Product 1", toBigDecimal(1.5)))
      val p2 = pr.save(Product(null, "Product 2", toBigDecimal(98.65)))
      val p3 = pr.save(Product(null, "Product 3", toBigDecimal(100.34)))
      val p4 = pr.save(Product(null, "Product 4", null))
      val c1 = cr.save(Customer(null, "Customer 1"))
      val c2 = cr.save(Customer(null, "Customer 2"))
      val now = toTimestamp(LocalDateTime.now())
      val yesterday = toTimestamp(LocalDateTime.now().minusDays(1))
      val o1 = or.save(Order(null, c1, now))
      val o2 = or.save(Order(null, c1, now))
      val o3 = or.save(Order(null, c1, yesterday))
      val o4 = or.save(Order(null, c2, now))
      val o5 = or.save(Order(null, c2, yesterday))
      // TODO: Why this call produces select and just then insert?
      opr.save(OrderProduct(OrderProductPK(o1.getId, p1.getId), null, null))
      opr.save(OrderProduct(OrderProductPK(o1.getId, p2.getId), null, null))
      opr.save(OrderProduct(OrderProductPK(o1.getId, p3.getId), null, null))
      opr.save(OrderProduct(OrderProductPK(o1.getId, p4.getId), null, null))
      opr.save(OrderProduct(OrderProductPK(o2.getId, p1.getId), null, null))
      opr.save(OrderProduct(OrderProductPK(o2.getId, p1.getId), null, null))
      opr.save(OrderProduct(OrderProductPK(o3.getId, p2.getId), null, null))
      opr.save(OrderProduct(OrderProductPK(o4.getId, p4.getId), null, null))

      println(pr.findAll().asScala.toList)
      println(pr.findAll(new PageRequest(1,2)).asScala.toList)
      println(opr.findAll().asScala.toList)
      opr.findAll().asScala.toList foreach println
      println(or.orderTotalPrice(1))
    }
  }
}

object Application extends App {

  SpringApplication.run(classOf[Application])

}
