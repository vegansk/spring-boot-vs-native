package app

package object models {

  import javax.persistence._
  import scala.annotation.meta.beanGetter

  type _GeneratedValue = GeneratedValue @beanGetter
  type _Column = Column @beanGetter
  type _Id = Id @beanGetter
  type _OneToOne = OneToOne @beanGetter
  type _ManyToOne = ManyToOne @beanGetter
  type _JoinColumn = JoinColumn @beanGetter

}
