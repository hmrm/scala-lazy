//God only knows if this is a good idea
package com.hmaxwell.lazydata
package object Lazy {
  case class Lazy[A](a: Unit => A){
    //Memoization
    lazy val value = a()
    def apply(): A = value

    //I think this is a monad as long as "a" is always referentially transparent, but I can't be bothered to actually check the laws.
    def map[B](f: A => B): Lazy[B] = new Lazy(a andThen f)
    def flatMap[B](f: A => Lazy[B]): Lazy[B] = {
      val extract: Lazy[B] => B = _.apply()
      new Lazy(a andThen f andThen extract)
    }
  }

  implicit object lazyConversions {
    implicit def strictToLazy[A](a: A): Lazy[A] = new Lazy(_ => a)
    implicit def lazyToStrict[A](a: Lazy[A]): A = a()
  }
}
