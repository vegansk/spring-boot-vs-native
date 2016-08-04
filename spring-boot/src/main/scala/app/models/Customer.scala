package app.models

import javax.persistence._
import scala.beans.BeanProperty

@Entity
@Table(name="customers")
case class Customer(
  @_Id
  @_GeneratedValue(strategy=GenerationType.AUTO)
  @BeanProperty
  var id: java.lang.Long,

  @BeanProperty
  var name: String
) {
  def this() = this(null, null)
}
