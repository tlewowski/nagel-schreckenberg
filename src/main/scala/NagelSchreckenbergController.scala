import org.scalajs.dom._
import org.scalajs.dom.html.{Button, Div}
import ui._

import scala.util.{Random, Try}
import scala.scalajs.js.timers._

object NagelSchreckenbergController {
  val rowWidth = 150
  var timeout : SetTimeoutHandle = null
  var iterationNumber = 1

  def setupConfigBox() = {
    ConfigurationBox.create((trigger: SimulationTrigger) => {
      iterationNumber = 1
      if(timeout != null) {
        clearTimeout(timeout)
        timeout = null
        trigger.stopSimulation()

      } else {
        val dataBox = document.getElementById("data-box")
        dataBox.removeChild(document.getElementById("data-table"))
        dataBox.appendChild(setupTable)
        simulate()
        trigger.startSimulation()
      }
    })
  }

  private def setupStatistics() = {
    val container = document.createElement("div").asInstanceOf[Div]
    container.id = "statistics-box"

    val countBox = document.createElement("div")
    countBox.id = "count-box"
    container.appendChild(countBox)

    val avgSpeedBox = document.createElement("div")
    avgSpeedBox.id = "speed-box"
    container.appendChild(avgSpeedBox)

    val variancePerVehicleBox = document.createElement("div")
    variancePerVehicleBox.id = "variance-box"
    container.appendChild(variancePerVehicleBox)

    val iteration = document.createElement("div")
    iteration.id = "iteration-box"
    container.appendChild(iteration)

    container
  }


  def setupDataTable = {
    val dataBox = document.createElement("div")
    dataBox.id = "data-box"
    dataBox.appendChild(setupTable)

    dataBox
  }

  private def setupTable = {
    val dataTable = document.createElement("table")
    dataTable.id = "data-table"

    val header = document.createElement("thead")
    dataTable.appendChild(header)
    val row = document.createElement("tr")
    header.appendChild(row)

    val iteration = document.createElement("td")
    iteration.textContent = "Nr iteracji"
    val avg = document.createElement("td")
    avg.textContent = "Średnia prędkość"
    val div = document.createElement("td")
    div.textContent = "Odchylenie standardowe prędkości"

    row.appendChild(iteration)
    row.appendChild(avg)
    row.appendChild(div)

    dataTable
  }

  def setupUI() : Element = {
    val wholeUi = document.createElement("div").asInstanceOf[Div]
    wholeUi.appendChild(setupConfigBox)
    wholeUi.appendChild(setupStatistics)

    val simulationBox = document.createElement("canvas")
    simulationBox.id = "simulation-canvas"
    wholeUi.appendChild(simulationBox)
    wholeUi.appendChild(setupDataTable)

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
      iterationNumber = iterationNumber + 1
      val state = iteration.next()
      drawRoad(state.roadState)
      scheduleNextStep(state)
    }
  }

  def addDataRow(iterationNumber: Int, avgSpeed: Double, dev: Double) = {
    val table = document.getElementById("data-table")
    val row = document.createElement("tr")
    table.appendChild(row)

    val it = document.createElement("td")
    it.textContent = iterationNumber.toString
    row.appendChild(it)

    val avg = document.createElement("td")
    avg.textContent = avgSpeed.toString
    row.appendChild(avg)

    val div = document.createElement("td")
    div.textContent = dev.toString
    row.appendChild(div)
  }

  def fillStats(road: Vector[Option[Int]]) = {
    val count = document.getElementById("count-box")
    val speed = document.getElementById("speed-box")
    val variance = document.getElementById("variance-box")
    val iteration = document.getElementById("iteration-box")

    val nonempty = road.filter(p => p.isDefined)
    count.textContent = "Liczba pojazdów: " + nonempty.length.toString + " (komórek drogi: " + road.length + ")"
    val avgSpeed = nonempty.foldLeft(0.0)((a,b) => a + b.get) / nonempty.length
    speed.textContent = "Średnia prędkość: " + avgSpeed.toString
    val dev = Math.sqrt(nonempty.foldLeft(0.0)((a,b) => Math.pow(b.get - avgSpeed, 2) + a) / nonempty.length)
    variance.textContent = "Odchylenie standardowe populacji: " + dev.toString
    iteration.textContent = "Iteracja: " + iterationNumber.toString

    if(iterationNumber <= CountedIterationsBox.getValue) {
      addDataRow(iterationNumber, avgSpeed, dev)
    }

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