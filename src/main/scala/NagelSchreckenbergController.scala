package own.controllers
import org.scalajs.dom._
import org.scalajs.dom.html.{Button, Div, Input}
import own.models.NagelSchreckenberg

import scala.util.{Random, Try}
import scala.scalajs.js.timers._

object NagelSchreckenbergController {
  val rowWidth = 150
  var timeout : SetTimeoutHandle = null

  def wrapped(n: Node) = {
    val c = document.createElement("div")
    c.appendChild(n)
  }

  def setupConfigBox(): Element = {
    val configBox = document.createElement("div")
    configBox.id = "container"
    configBox.appendChild(WidthBox.setupUI)
    configBox.appendChild(DensityBox.setupUI)
    configBox.appendChild(ProbabilityBox.setupUI)
    configBox.appendChild(SpeedBox.setupUI)
    configBox.appendChild(SpeedStepBox.setupUI)
    configBox.appendChild(MinimumTimeoutBox.setupUI)

    val starter = document.createElement("button").asInstanceOf[Button]
    starter.onclick = (_) => {
      if(timeout != null) {
        clearTimeout(timeout)
        timeout = null
        starter.textContent = "Od nowa"
      } else {
        simulate()
        starter.textContent = "Stop"
      }
    }

    starter.textContent = "Rozpocznij symulację"
    configBox.appendChild(wrapped(starter))

    configBox
  }

  private def setupStatistics() = {
    val container = document.createElement("div").asInstanceOf[Div]

    val countBox = document.createElement("div")
    countBox.id = "count-box"
    container.appendChild(countBox)

    val avgSpeedBox = document.createElement("div")
    avgSpeedBox.id = "speed-box"
    container.appendChild(avgSpeedBox)

    container
  }

  def setupUI() : Element = {
    val wholeUi = document.createElement("div").asInstanceOf[Div]
    wholeUi.appendChild(setupConfigBox)
    wholeUi.appendChild(setupStatistics)

    val simulationBox = document.createElement("canvas")
    simulationBox.id = "simulation-canvas"
    wholeUi.appendChild(wrapped(simulationBox))

    wholeUi
  }


  def simulate() : Unit = {
    val model = new NagelSchreckenberg.RoadSimulator()
      .createSimulation(Stream.continually(Random.nextDouble),
        DensityBox.getValue,
        ProbabilityBox.getValue,
        SpeedBox.getValue.toInt,
        SpeedStepBox.getValue.toInt,
        rowWidth*WidthBox.getValue.toInt)
    drawRoad(model.roadState)
    scheduleNextStep(model)
  }

  def scheduleNextStep(iteration: NagelSchreckenberg.Iteration) : Unit = {
    timeout = setTimeout(MinimumTimeoutBox.getValue) {
      val state = iteration.next()
      drawRoad(state.roadState)
      scheduleNextStep(state)
    }
  }

  def fillStats(road: Vector[Option[Int]]) = {
    val count = document.getElementById("count-box")
    val speed = document.getElementById("speed-box")

    val nonempty = road.filter(p => p.isDefined)
    count.textContent = "Liczba pojazdów: " + nonempty.length.toString + " (komórek drogi: " + road.length + ")"
    val avgSpeed = nonempty.foldLeft(0.0)((a,b) => a + b.get)
    speed.textContent = "Średnia prędkość: " + (avgSpeed / nonempty.length).toString
  }

  def drawRoad(road: Vector[Option[Int]]): Unit = {
    val sizeInPixels = 5
    val rows = road.length / rowWidth
    val canvas = document.getElementById("simulation-canvas").asInstanceOf[html.Canvas]
    val ctx = canvas.getContext("2d").asInstanceOf[CanvasRenderingContext2D]
    canvas.width = rowWidth * sizeInPixels
    canvas.height = rows * sizeInPixels
    ctx.strokeStyle = "red"
    ctx.lineWidth = 20

    road.zipWithIndex.foreach(r => {
      if (r._1.isDefined) {
        ctx.fillRect(sizeInPixels*(r._2 % rowWidth), sizeInPixels*(r._2 / rowWidth), sizeInPixels, sizeInPixels)
      }
      else {
        ctx.clearRect(sizeInPixels*(r._2 % rowWidth), sizeInPixels*(r._2 / rowWidth), sizeInPixels, sizeInPixels)
      }
    })

    ctx.stroke

    fillStats(road)
  }
}

object WidthBox extends LabeledBox {
  override def getId = "width"
  override def getLabel = "Liczba wierszy jezdni"
  override def getMin: Double = 1
  override def getMax: Double = 1000
  override def getStep: Double = 1
  override def getDefault: Double = 150
}

object DensityBox extends LabeledBox {
  override def getId = "density"
  override def getLabel = "Gęstość pojazdów"
  override def getMin: Double = 0
  override def getMax: Double = 1
  override def getStep: Double = 0.0001
  override def getDefault: Double = 0.1
}

object ProbabilityBox extends LabeledBox {
  override def getId = "probability"
  override def getLabel = "Prawdopodobieństwo spowolnienia"
  override def getMin: Double = 0
  override def getMax: Double = 1
  override def getStep: Double = 0.0001
  override def getDefault: Double = 0.2
}

object SpeedBox extends LabeledBox {
  override def getId: String = "max-speed"
  override def getLabel: String = "Maksymalna prędkość"
  override def getMin: Double = 1
  override def getMax: Double = 140
  override def getStep: Double = 1
  override def getDefault: Double = 5
}

object SpeedStepBox extends LabeledBox {
  override def getId: String = "speed-step"
  override def getLabel: String = "Krok spowalniania"
  override def getMin: Double = 1
  override def getMax: Double = 140
  override def getStep: Double = 1
  override def getDefault: Double = 1
}

object MinimumTimeoutBox extends LabeledBox {
  override def getId: String = "minimum-timeout"
  override def getLabel: String = "Minimalne opóźnienie między kolejnymi ramkami (ms)"
  override def getMin: Double = 0
  override def getMax: Double = 10000
  override def getStep: Double = 1
  override def getDefault: Double = 0
}

trait LabeledBox {
  def getId : String
  def getLabel: String
  def getMin: Double
  def getMax: Double
  def getStep: Double
  def getDefault: Double

  def getValue: Double = {
    val element = document.getElementById(getId).asInstanceOf[Input]
    Try { element.value.toDouble }.getOrElse(getDefault)
  }

  def setupUI: Element = {
    val container = document.createElement("div")
    container.id = "container-" + getId

    val label = document.createElement("label")
    label.setAttribute("for", getId)
    label.textContent = getLabel

    val input = document.createElement("input").asInstanceOf[Input]
    input.id = getId
    input.`type` = "number"
    input.min = getMin.toString
    input.max = getMax.toString
    input.step = getStep.toString
    input.defaultValue = getDefault.toString
    input.maxLength = 7

    container.appendChild(label)
    container.appendChild(input)

    container
  }
}
