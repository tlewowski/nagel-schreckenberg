package ui

import org.scalajs.dom.{Element, document}

object ConfigurationBox {
  def create(triggerCallback: SimulationTrigger => Unit): Element = {
    val configBox = document.createElement("form")
    configBox.id = "params-box"
    configBox.appendChild(WidthBox.setupUI(SimulationTriggeringButtons))
    configBox.appendChild(DensityBox.setupUI(SimulationTriggeringButtons))
    configBox.appendChild(ProbabilityBox.setupUI(SimulationTriggeringButtons))
    configBox.appendChild(SpeedBox.setupUI(SimulationTriggeringButtons))
    configBox.appendChild(SpeedStepBox.setupUI(SimulationTriggeringButtons))
    configBox.appendChild(MinimumTimeoutBox.setupUI(SimulationTriggeringButtons))
    configBox.appendChild(CountedIterationsBox.setupUI(SimulationTriggeringButtons))
    configBox.appendChild(SimulationTriggeringButtons.setupUI(triggerCallback))

    configBox
  }
}
