package ui

import org.scalajs.dom.{Element, document}
import org.scalajs.dom.html.{Button, Div}

object SimulationTriggeringButtons extends SimulationTrigger {
  var invalid = false
  var started = false
  override def startSimulation(): Unit = {
    Option(document.getElementById("simulation-trigger").asInstanceOf[Button])
      .foreach(b => {
        b.textContent = "Stop"
        b.classList.remove("btn-primary")
        b.classList.add("btn-danger")
        started = true
      })
  }

  override def stopSimulation(): Unit = {
    Option(document.getElementById("simulation-trigger").asInstanceOf[Button])
      .foreach(b => {
        b.textContent = "Od nowa"
        b.classList.add("btn-primary")
        b.classList.remove("btn-danger")
        started = false
        revalidate()
      })
  }

  override def revalidate(): Unit = {
    if(WidthBox.isValid && DensityBox.isValid && ProbabilityBox.isValid && SpeedBox.isValid &&
      SpeedStepBox.isValid && MinimumTimeoutBox.isValid && CountedIterationsBox.isValid) {
      invalid = false
      Option(document.getElementById("simulation-trigger").asInstanceOf[Button])
        .foreach(b => {
          b.classList.remove("disabled")
          b.disabled = false
        })
    } else {
      invalid = true
      Option(document.getElementById("simulation-trigger").asInstanceOf[Button])
        .foreach(b => {
          b.classList.add("disabled")
          b.disabled = true
        })
    }
  }

  def setupUI(triggerCallback: (SimulationTrigger) => Unit ): Element = {
    val box = document.createElement("div").asInstanceOf[Div]
    box.id = "trigger-box"

    val starter = document.createElement("button").asInstanceOf[Button]
    starter.onclick = (_) => triggerCallback(SimulationTriggeringButtons)

    starter.textContent = "Rozpocznij symulację"
    starter.`type` = "button"
    starter.id = "simulation-trigger"
    starter.classList.add("btn")
    starter.classList.add("btn-primary")

    box.appendChild(starter)

    box
  }
}