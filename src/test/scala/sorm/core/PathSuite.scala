package sorm.core

import org.scalatest.{FunSuite, Matchers}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner


@RunWith(classOf[JUnitRunner])
class PathSuite extends FunSuite with Matchers {
  
  import Path._
  
  test("pathAndRemainder failure"){
    pending
  }
  test("pathAndRemainder braced parsing"){
    partAndRemainder("(asdf)") should equal (Part.Braced("asdf"), "")
    partAndRemainder("(asdf).sdf") should equal (Part.Braced("asdf"), ".sdf")
    partAndRemainder("(342).sdf") should equal (Part.Braced("342"), ".sdf")
  }
  test("pathAndRemainder dotted parsing"){
    partAndRemainder("sdf") should equal (Part.Dotted("sdf"), "")
    partAndRemainder("sdf.dksfje") should equal (Part.Dotted("sdf"), ".dksfje")
    partAndRemainder(".sdf.dksfje") should equal (Part.Dotted("sdf"), ".dksfje")
  }
}
