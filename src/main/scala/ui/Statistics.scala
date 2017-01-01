package ui

import org.scalajs.dom.document
import org.scalajs.dom.html.{Div, Element, Span}
import ui.helper.HtmlCreator

object Statistics {
  def setField[T](id: String, format: String)(value: Double): Unit = {
    Option(document.getElementById(id))
      .foreach(elem => {
        elem.textContent = format.format(value)
      })

  }

  def setAvgSpeed(avgSpeed: Double): Unit = setField("speed", "%8.5f")(avgSpeed)
  def setStdDev(stdDev: Double): Unit = setField("stddev", "%8.5f")(stdDev)
  def setIteration(iteration: Int): Unit = setField("iteration", "%5.0f")(iteration.toDouble)
  def setCount(vehiclesCount: Int, roadLength: Int): Unit = {
    setField("vehicles-count", "%5.0f")(vehiclesCount.toDouble)
    setField("road-count", "%5.0f")(vehiclesCount.toDouble)
  }

  def setupUI(): Element = {
    val container = HtmlCreator.create[Div]("statistics-box")
    container.classList.add("jumbotron")
    container.appendChild(StatBox.createStat("road-count", "Liczba komórek drogi: "))
    container.appendChild(StatBox.createStat("vehicles-count", "Liczba pojazdów: "))
    container.appendChild(StatBox.createStat("speed", "Średnia prędkość: "))
    container.appendChild(StatBox.createStat("stddev", "Odchylenie standardowe prędkości: "))
    container.appendChild(StatBox.createStat("iteration", "Iteracja: "))

    container
  }

  object StatBox {
    def createStat(valueName: String, constantText: String): Div = {
      val box = HtmlCreator.create[Div](valueName + "-box")
      val textSpan = HtmlCreator.create[Span]("text-" + valueName)
      textSpan.classList.add("label")
      textSpan.classList.add("label-info")
      textSpan.textContent = constantText
      box.appendChild(textSpan)

      val valueSpan = HtmlCreator.create[Span](valueName)
      valueSpan.textContent = "0"
      box.appendChild(valueSpan)

      box
    }
  }
}
