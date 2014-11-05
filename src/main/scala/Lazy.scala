//God only knows if this is a good idea
package com.hmaxwell.lazydata
package object Lazy {
  case class Lazy[A](a: => A) extends (Unit => A) {
    //Memoization
    lazy val value = a
    def apply(): A = value

    def flatten[B](implicit ev: A <:< Lazy[B]): Lazy[B] = Lazy(value())
    
    def map[B](f: A => B): Lazy[B] = new Lazy(f(a))
    
    def flatMap[B](f: A => Lazy[B]): Lazy[B] = (map andThen flatten)(f)
  }

  implicit object lazyConversions {
    implicit def strictToLazy[A](a: => A): Lazy[A] = new Lazy(a)
    implicit def lazyToStrict[A](a: Lazy[A]): A = a()
  }
}
