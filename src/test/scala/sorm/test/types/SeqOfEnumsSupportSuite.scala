package sorm.test.types

import org.scalatest.{FunSuite, Matchers}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import sorm._

import samples._
import sorm.test.MultiInstanceSuite

@RunWith(classOf[JUnitRunner])
class SeqOfEnumsSupportSuite extends FunSuite with Matchers with MultiInstanceSuite {
  import SeqOfEnumsSupportSuite._

  def entities =  Set() + Entity[A]()
  instancesAndIds foreach { case (db, dbId) =>

    val a1 = db.save(A(B.Two :: Nil))
    val a2 = db.save(A(B.Two :: B.Two :: B.One :: Nil))
    val a3 = db.save(A(B.Three :: Nil))
    val a4 = db.save(A(B.Two :: Nil))
    val a5 = db.save(A(Nil))

    test(dbId + " - Equal query"){
      db.query[A].whereEqual("a", Seq(B.Two)).fetch()
        .should(
          have size (2) and
          contain (a4)
        )
    }
    test(dbId + " - Equal empty Seq query"){
      db.query[A].whereEqual("a", Seq()).fetch()
        .should(
          contain (a5) and not contain (a4)
        )
    }
    test(dbId + " - Not equals empty Seq query"){
      db.query[A].whereNotEqual("a", Seq()).fetch()
        .should(
          not be 'empty and
          not contain (a5)
        )
    }
    test(dbId + " - Not equals query"){
      db.query[A].whereNotEqual("a", Seq(B.Three)).fetch()
        .should(
          not be 'empty and
          not contain (a3)
        )
    }
    test(dbId + " - Equal inexistent Seq query"){
      db.query[A].whereEqual("a", Seq(B.Three, B.Three)).fetch()
        .should(
          be ('empty)
        )
    }
    test(dbId + " - Not equals inexistent Seq query"){
      db.query[A].whereNotEqual("a", Seq(B.Three, B.Three)).fetch()
        .should(
          not be ('empty)
        )
    }
  }
}
object SeqOfEnumsSupportSuite {

  case class A ( a : Seq[B.Value] )
  object B extends Enumeration {
    val One, Two, Three = Value
  }


}