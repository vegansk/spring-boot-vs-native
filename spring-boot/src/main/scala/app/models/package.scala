package app

package object models {

  import javax.persistence.{ Column, GeneratedValue, Id }
  import scala.annotation.meta.beanGetter

  type _GeneratedValue = GeneratedValue @beanGetter
  type _Column = Column @beanGetter
  type _Id = Id @beanGetter

}
