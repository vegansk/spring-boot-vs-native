import akka.actor.{ Actor, ActorSystem, Props }
import akka.event.Logging
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import scalikejdbc._
import scalikejdbc.config._
import spray.can.Http
import spray.routing._
import Service._
import spray.http.MediaTypes._
import upickle.default._
import scala.concurrent.duration._
import com.typesafe.config._

import app.db._
import app.models._

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
  val log = Logging.getLogger(system, this)

  val httpConfig = ConfigFactory.load.getConfig("http")

  val service = system.actorOf(Props[HttpServiceActor], "my-service")

  IO(Http) ? Http.Bind(service, interface = httpConfig.getString("interface"), port = httpConfig.getInt("port"))

  DBs.setupAll()

  val ddl = io.Source.fromURL(getClass.getResource("/createdb.sql")).mkString

  DB localTx { s =>
    s.execute(ddl)
  }

  log.info(s"Records before initialization ${Customers.count}")
  val id = Customers.create(Customer(0, "Mike Wazovsky"))
  log.info(s"Inserted id = ${id}")
  Customers.find(id).map(v => log.info(s"Found ${v.name}"))
}
