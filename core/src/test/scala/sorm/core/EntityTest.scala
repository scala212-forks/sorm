package sorm.core

import org.scalatest.FunSuite

@org.junit.runner.RunWith(classOf[org.scalatest.junit.JUnitRunner])
class EntityTest extends FunSuite with Exports {

  test("Mixin persisted"){
    // Having this class:
    case class A( a : Int, b : String )

    // NOTE: please note that writing `val e: Entity[A] = ...` will make the code fail to compile
    // because Entity[A] doesn't guarantee that .copy(...) will have the id member

    // triggering the macro conversion with the following:
    val e = entity[ A ]( Set() )
    // should result in:
    // val e = new Entity[ A ]{
    //   // the value is taken from here: `entity[ A ]( Set(), -> Set() <- )`
    //   val indexed : Set[ Key[ A ] ] = Set()
    //   // the value is taken from here: `entity[ A ]( -> Set() <-, Set() )`
    //   val unique : Set[ Key[ A ] ] = Set()
    //   def mixinPersisted( value : A, idValue : Long )
    //     = new A( value.a, value.b ) with Persisted {
    //         val id = idValue
    //         // a copy of `A` with `Persisted` mixed in
    //         override def copy
    //           ( a : Int = a,
    //             b : String = b )
    //           : A with Persisted
    //           = mixinPersisted( A(a, b), id )
    //         // product elements of `A` prepended with `id`
    //         override def productElement( n : Int ) : Any = n match {
    //           case 0 => id
    //           case 1 => a
    //           case 2 => b
    //           case _ => throw new IndexOutOfBoundsException( n.toString )
    //         }
    //         // product arity of `A` plus 1 (implying `id`)
    //         override def productArity = 3
    //         override def equals( that : Any ) = that match {
    //           case that : Persisted => id == that.id && super.equals( that )
    //           case _ => false
    //         }
    //       }
    // }

    // Tests
    val value = A( 2, "ABC" )

    // Has the id prooperty and its set appropriately:
    assert(e.mixinPersisted(value, 1).id == 1)
    // A copy retains the id property:
    assert(e.mixinPersisted(value, 2).copy(50).id == 2)
    // A copy correctly updates properties:
    assert(e.mixinPersisted(value, 3).copy(50).a == 50)
  }

}