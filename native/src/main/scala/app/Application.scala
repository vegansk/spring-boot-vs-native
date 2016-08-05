package app

import akka.actor.{ Actor, ActorSystem, Props }
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import spray.can.Http
import spray.routing._
import Service._
import spray.http.MediaTypes._
import upickle.default._
import scala.concurrent.duration._
import com.typesafe.config._
import slick.driver.H2Driver.api._
import scala.concurrent.ExecutionContext.Implicits.global
import java.sql.Timestamp
import java.time.LocalDateTime
import scala.concurrent.{Future, Await}
import scala.concurrent.duration.Duration
import tables._, queries._

class HttpServiceActor extends Actor with HttpService {

  def actorRefFactory = context

  def receive = runRoute {
    respondWithMediaType(`application/json`) {
      (get & path("greeting") & parameter('name ? "World")) { name =>
        complete(write(greeting(name)))
      }
    }
  }
}

object Application extends App {
  implicit val system = ActorSystem("on-spray-can")
  implicit val timeout = Timeout(5.seconds)

  val db = Database.forConfig("h2mem1")

  val schemaSetup = (
    customers.schema ++
    orders.schema ++
    products.schema ++
    orderProducts.schema
  ).create

  def toTimestamp(ldt: LocalDateTime): Timestamp = Timestamp.valueOf(ldt)

  val fillDb = for {
    p1 <- insertProduct(Product(None, "Product 1", Some(1.5)))
    p2 <- insertProduct(Product(None, "Product 2", Some(98.65)))
    p3 <- insertProduct(Product(None, "Product 3", Some(100.34)))
    p4 <- insertProduct(Product(None, "Product 4", None))
    c1 <- insertCustomer(Customer(None, "Customer 1"))
    c2 <- insertCustomer(Customer(None, "Customer 2"))
    now = toTimestamp(LocalDateTime.now())
    yesterday = toTimestamp(LocalDateTime.now().minusDays(1))
    o1 <- insertOrder(Order(None, c1, now))
    o2 <- insertOrder(Order(None, c1, now))
    o3 <- insertOrder(Order(None, c1, yesterday))
    o4 <- insertOrder(Order(None, c2, now))
    o5 <- insertOrder(Order(None, c2, yesterday))
    _ <- addProductToOrder(p1, o1)
    _ <- addProductToOrder(p2, o1)
    _ <- addProductToOrder(p3, o1)
    _ <- addProductToOrder(p4, o1)
    _ <- addProductToOrder(p1, o2)
    _ <- addProductToOrder(p1, o2)
    _ <- addProductToOrder(p2, o3)
    _ <- addProductToOrder(p4, o4)
  } yield ()

  val f = (for {
    _ <- db.run(schemaSetup)
    _ <- db.run(fillDb)
    allProducts <- db.run(products.result)
    _ = println(allProducts)
    productsFirstPage <- db.run(productsPaginated(1, 2))
    _ = println(productsFirstPage)
    firstOrderPrice <- db.run(orderTotalPrice(1))
    _ = println(firstOrderPrice)
    customer1Orders <- db.run(customerOrders(1))
    _ = println(customer1Orders)
    customersWhoOrderedProduct3 <- db.run(customersWhoOrderedTheProduct(3))
    _ = println(customersWhoOrderedProduct3)
    customer1OrdersTotal <- db.run(customerOrdersTotal(1))
    _ = println(customer1OrdersTotal)
  } yield ())

  Await.result(f, Duration.Inf)

  val httpConfig = ConfigFactory.load.getConfig("http")
  val service = system.actorOf(Props[HttpServiceActor], "my-service")
  IO(Http) ? Http.Bind(service, interface = httpConfig.getString("interface"), port = httpConfig.getInt("port"))
}
