package sorm.test.features

import org.scalatest.{FunSuite, Matchers}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import sorm._
import sorm.test.MultiInstanceSuite

object MultithreadingTest {
  case class A (a : Int)
}
@RunWith(classOf[JUnitRunner])
class MultithreadingTest extends FunSuite with Matchers with MultiInstanceSuite {
  import MultithreadingTest._

  def entities =  Set() + Entity[A]()
  instancesAndIds foreach { case (db, dbId) =>

    val a1 = db.save(A(1))
    val a2 = db.save(A(3))
    val a3 = db.save(A(0))
    val a4 = db.save(A(3000))

    test(dbId + " - Parallel queries"){
      Seq(0,1,2,3).par.flatMap{ i => db.query[A].whereEqual("a", i).fetchOne() }.seq
        .should(contain(a1) and contain(a3) and not contain(a4))
    }
    test(dbId + " - Parallel saving"){
      pending
    }

  }
}
