package app.models

import javax.persistence._
import scala.beans.BeanProperty

@Entity
@Table(name="customers")
case class Order(
  @_Id
  @_GeneratedValue(strategy=GenerationType.AUTO)
  @BeanProperty
  var id: java.lang.Long,

  @BeanProperty
  @_ManyToOne(optional = false)
  @_JoinColumn(name = "customer_id")
  var customer: Customer
) {
  def this() = this(null, null)
}
