package sorm.test.types

import org.scalatest.{FunSuite, Matchers}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import sorm._
import sext._, embrace._
import sorm.test.MultiInstanceSuite

@RunWith(classOf[JUnitRunner])
class OptionSupportSuite extends FunSuite with Matchers with MultiInstanceSuite {
  import OptionSupportSuite._

  def entities = Entity[EntityWithOptionInOption]() :: Nil

}
object OptionSupportSuite {

  case class EntityWithOptionInOption
    ( optionInOption : Option[Option[Int]] )

}

