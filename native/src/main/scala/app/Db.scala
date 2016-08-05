package app

import slick.driver.H2Driver.api._

object db {

  class CustomersTable(tag: Tag) extends Table[Customer](tag, "customers") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")

    def * = (id.?, name) <> (Customer.tupled, Customer.unapply)
  }

  val customers = TableQuery[CustomersTable]

}
