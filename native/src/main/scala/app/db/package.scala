package app

import scalikejdbc._

package object db {

  import app.models._

  trait Table[A] extends SQLSyntaxSupport[A] {
    def apply(rn: ResultName[A])(rs: WrappedResultSet): A = ???
    def apply(e: SyntaxProvider[A])(rs: WrappedResultSet): A = apply(e.resultName)(rs)
    val t = syntax("t")
  }

  object Customers extends Table[Customer] {
    override def apply(rn: ResultName[Customer])(rs: WrappedResultSet): Customer =
      autoConstruct(rs, rn)

    def create(c: Customer)(implicit s: DBSession = AutoSession): Long = {
      withSQL {
        insertInto(Customers).namedValues(
          Customers.column.name -> c.name
        )
      }.updateAndReturnGeneratedKey.apply()
    }

    def save(c: Customer)(implicit s: DBSession = AutoSession) = {
      withSQL {
        update(Customers).set(
          Customers.column.name -> c.name
        ).where.eq(Customers.column.id, c.id)
      }
    }.update.apply()

    def find(id: Long)(implicit s: DBSession = AutoSession): Option[Customer] =
      withSQL {
        select.from(Customers as t).where.eq(t.id, id)
      }.map(Customers(t)).single.apply()

    def count(implicit s: DBSession = AutoSession): Long = sql"select count(*) from customers".map(_.long(1)).single.apply().get

  }
}
