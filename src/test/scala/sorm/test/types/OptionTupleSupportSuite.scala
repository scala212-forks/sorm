package sorm.test.types

import org.scalatest.{FunSuite, Matchers}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import sorm._
import sext._, embrace._
import sorm.test.MultiInstanceSuite

@RunWith(classOf[JUnitRunner])
class OptionTupleSupportSuite extends FunSuite with Matchers with MultiInstanceSuite {

  import OptionTupleSupportSuite._

  def entities = Set() + Entity[A]()
  instancesAndIds foreach { case (db, dbId) =>
    val a1 = db.save(A( None ))
    val a2 = db.save(A( Some(2 -> None) ))
    val a3 = db.save(A( Some(56 -> Some("asdf")) ))

    test(dbId + " - top none"){
      db.fetchById[A](a1.id).a shouldEqual None
    }
    test(dbId + " - deep none"){
      db.fetchById[A](a2.id).a shouldEqual Some(2 -> None)
    }
    test(dbId + " - deep some"){
      db.fetchById[A](a3.id).a shouldEqual Some(56 -> Some("asdf"))
    }
  }

}
object OptionTupleSupportSuite {

  case class A
    ( a : Option[(Int, Option[String])] )

}