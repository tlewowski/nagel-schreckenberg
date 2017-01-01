package ui

import org.scalajs.dom.document
import org.scalajs.dom.html.{Div, Element, Span}
import ui.helper.HtmlCreator

object Statistics {
  def setStdDev(dev: Double): Unit = {
    Option(document.getElementById("stddev"))
      .foreach(elem => {
        elem.textContent = f"$dev%8.5f"
      })
  }

  def setIteration(iterationNumber: Int): Unit = {
    Option(document.getElementById("iteration"))
      .foreach(elem => {
        elem.textContent = iterationNumber.toString
      })
  }

  def setCount(vehicles: Int, road: Int): Unit = {
    Option(document.getElementById("road-count"))
      .foreach(elem => {
        elem.textContent = road.toString
      })

    Option(document.getElementById("vehicles-count"))
      .foreach(elem => {
        elem.textContent = vehicles.toString
      })
  }

  def setAvgSpeed(avgSpeed: Double): Unit = {
    Option(document.getElementById("speed"))
      .foreach(elem => {
        elem.textContent = f"$avgSpeed%8.5f"
      })
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
