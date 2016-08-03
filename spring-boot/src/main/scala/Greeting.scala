package app

import scala.beans.BeanProperty

case class Greeting(
  @BeanProperty id: Long,
  @BeanProperty content: String)
