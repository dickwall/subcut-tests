package com.escalatesoft.subcut.test

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import com.escalatesoft.subcut.inject._

/**
 * Created by IntelliJ IDEA.
 * User: dick
 * Date: 10/5/11
 * Time: 2:56 PM
 * To change this template use File | Settings | File Templates.
 */

class CompilerPluginTest extends FunSuite with ShouldMatchers {

  test("Use defined binding, supply no constructor parameter") {
    implicit val bindings = AnimalModule
    val ad = new AutoAnimalDomain
    ad.soundsFromDomain should be ("Woof Woof Woof ")
  }

  test("Use supplied animal in constructor parameter") {
    implicit val bindings = AnimalModule
    val ad = new AutoAnimalDomain(Some(new Frog))
    ad.soundsFromDomain should be ("Ribbit Ribbit Ribbit ")
  }

  test("Force an unbind, should fail with a binding exception if no parameter supplied but work otherwise") {
    AnimalModule.modifyBindings { implicit animalModule =>
      animalModule.unbind[Animal]

      // should still work for explicit parameter in constructor
      val ad = new AutoAnimalDomain(Some(new Frog))(animalModule)
      println((new Dog("fido")).toString)
      ad.soundsFromDomain should be ("Ribbit Ribbit Ribbit ")

      // but not for binding case any more
      intercept[BindingException] {
        val ad2 = new AutoAnimalDomain()(animalModule)
      }
    }
  }
}

trait Animal {
  def makeNoise(): String
}

case class Frog() extends Animal with AutoInjectable {
  def makeNoise() = "Ribbit"
}

case class Dog(name: String) extends Animal with AutoInjectable {
  def makeNoise() = "Woof"
}

object AnimalModule extends NewBindingModule(module => {
  module.bind [Animal] toModuleSingle { module => new Dog("fido")}
})

class Home

class AutoAnimalDomain(an: Option[Animal] = None) extends Home with AutoInjectable {
  val animal = injectIfMissing[Animal](an)

  def soundsFromDomain(): String = {
    (animal.makeNoise + " ") * 3
  }
}
