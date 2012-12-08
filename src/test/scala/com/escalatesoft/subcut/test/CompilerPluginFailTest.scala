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

class CompilerPluginFailTest extends FunSuite with ShouldMatchers {

  test("Use defined binding, supply no constructor parameter") {
    implicit val bindings = AnimalModuleF
    implicit val some: String = "hello"
    val ad = new AutoAnimalDomainF
    ad.soundsFromDomain should be ("Woof Woof Woof ")
  }

  test("Use supplied animal in constructor parameter") {
    implicit val bindings = AnimalModuleF
    implicit val some: String = "yo"
    val ad = new AutoAnimalDomainF(Some(new FrogF))
    ad.soundsFromDomain should be ("Ribbit Ribbit Ribbit ")
  }

  test("Force an unbind, should fail with a binding exception if no parameter supplied but work otherwise") {
    AnimalModuleF.modifyBindings { animalModule =>
      animalModule.unbind[Animal]

      // should still work for explicit parameter in constructor
      val ad = new AutoAnimalDomainF(Some(new FrogF))("ten")(animalModule)
      ad.soundsFromDomain should be ("Ribbit Ribbit Ribbit ")

    }
  }
}

trait AnimalF {
  def makeNoise(): String
}

class FrogF extends AnimalF {
  def makeNoise() = "Ribbit"
}

class DogF extends AnimalF {
  def makeNoise() = "Woof"
}

object AnimalModuleF extends NewBindingModule(module => {
  module.bind [AnimalF] to newInstanceOf [DogF]
})

class HomeF

class AutoAnimalDomainF(an: Option[AnimalF] = None)(implicit some: String) extends HomeF with AutoInjectable {
  val animal = injectIfMissing[AnimalF](an)

  val fred = some

  def soundsFromDomain(): String = {
    println(fred)
    println(some)
    (animal.makeNoise + " ") * 3
  }
}
