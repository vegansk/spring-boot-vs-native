package app.models

import javax.persistence._
import scala.beans.BeanProperty

@Entity
@Table(name="products")
case class Product(
  @_Id
  @_GeneratedValue(strategy=GenerationType.AUTO)
  @BeanProperty
  var id: java.lang.Long,

  @BeanProperty
  @_Column(nullable = false)
  var name: String,

  @BeanProperty
  var price: java.math.BigDecimal
) {
  def this() = this(null, null, null)
}
