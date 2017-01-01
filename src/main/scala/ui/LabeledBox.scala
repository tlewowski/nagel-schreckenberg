package ui

import org.scalajs.dom.html.{Div, Input}
import org.scalajs.dom.{Element, document}
import ui.helper.HtmlCreator

import scala.util.Try

trait LabeledBox {
  def getId: String
  def getLabel: String
  def getMin: Double
  def getMax: Double
  def getStep: Double
  def getDefault: Double

  def getValue: Double = {
      Option(document.getElementById(getId).asInstanceOf[Input])
        .map(elem => elem.value.toDouble)
        .getOrElse(getDefault)
  }

  def isValid: Boolean = {
    Option(document.getElementById(getId).asInstanceOf[Input])
      .map(elem => Try { elem.value.toDouble }.map(d => d <= getMax && d >= getMin).getOrElse(false))
      .getOrElse(false)
  }

  def setupUI(simulationTrigger: SimulationTrigger): Element = {
    val container = HtmlCreator.create[Div]("container-" + getId)
    container.classList.add("form-group")

    val label = document.createElement("label")
    label.setAttribute("for", getId)
    label.textContent = getLabel
    label.classList.add("control-label")

    val input = document.createElement("input").asInstanceOf[Input]
    input.classList.add("form-control")
    input.id = getId
    input.`type` = "number"
    input.min = getMin.toString
    input.max = getMax.toString
    input.step = getStep.toString
    input.defaultValue = getDefault.toString
    input.maxLength = 7

    input.onfocusout = (_) => {
      if(isValid) {
        markAsValid(container)
      } else {
        markAsInvalid(container)
      }
      simulationTrigger.revalidate()
    }

    val help = document.createElement("span")
    help.id="help-" + getId
    help.textContent = "Dopuszczalny zakres: [" + getMin + "," + getMax + "]"
    help.classList.add("help-block")

    input.onfocusin = (_) => {
      help.textContent = "Dopuszczalny zakres: [" + getMin + "," + getMax + "]"
      input.max = getMax.toString
    }


    container.appendChild(label)
    container.appendChild(input)
    container.appendChild(help)

    container
  }

  private def markAsInvalid(element: Element) = {
    element.classList.remove("has-success")
    element.classList.add("has-error")
  }

  private def markAsValid(element: Element) = {
    element.classList.remove("has-error")
    element.classList.add("has-success")
  }
}