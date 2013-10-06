//God only knows if this is a good idea
case class Lazy[A](a: Unit => A){
  lazy val value = a()
  def apply(): A = value

  def map[B](f: A => B): Lazy[B] = new Lazy(a andThen f)
  def flatMap[B](f: A => Lazy[B]): Lazy[B] = {
    val extract: Lazy[B] => B = _.apply()
    new Lazy(a andThen f andThen extract)
  }
}

object lazyConversions {
  implicit def strictToLazy[A](a: A): Lazy[A] = new Lazy(_ => a)
  implicit def lazyToStrict[A](a: Lazy[A]): A = a()
}
