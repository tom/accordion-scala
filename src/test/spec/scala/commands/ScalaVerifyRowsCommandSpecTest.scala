package scala.commands

import net.tommalone.accordion.{AccordionRunner, WithCommands, AddACommand}
import org.junit.runner.RunWith
import net.tommalone.accordion.commands.ScalaVerifyRowsCommand

@RunWith(classOf[AccordionRunner])
class ScalaVerifyRowsCommandSpecTest {

  @WithCommands(nameSpace = "http://www.tommalone.net")
  def addAssertContainsCommand(addACommand: AddACommand): AddACommand = {
    addACommand.addCommand("verifyRows", new ScalaVerifyRowsCommand())
    addACommand
  }

  def getPeople(): List[Person] = {
    List(new Person("Tom", 28, "Male"),
      new Person("Anna", 23, "Female"),
      new Person("James", 30, "Male"))
  }
}

case class Person(name: String, age: Int, gender: String) {

  def getName():String = name
  def getAge():Int = age
  def getGender():String = gender

}