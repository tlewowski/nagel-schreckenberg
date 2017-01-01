import org.scalajs.dom._
import org.scalajs.dom.html.{Button, Div}
import ui._

import scala.util.{Random, Try}
import scala.scalajs.js.timers._

object NagelSchreckenbergController {
  var timeout : SetTimeoutHandle = null
  var iterationNumber = 0

  def setupConfigBox() = {
    ConfigurationBox.create((trigger: SimulationTrigger) => {
      iterationNumber = 0
      if(timeout != null) {
        clearTimeout(timeout)
        timeout = null
        trigger.stopSimulation()

      } else {
        DataTable.clear()
        simulate()
        trigger.startSimulation()
      }
    })
  }

  def setupUI() : Element = {
    val wholeUi = document.createElement("div").asInstanceOf[Div]
    wholeUi.id = "application-box"

    val roadBox = Road.setupUI()
    wholeUi.appendChild(setupConfigBox())
    wholeUi.appendChild(Road.setupUI())

    val resultsBox = document.createElement("div")
    resultsBox.id = "results-box"

    resultsBox.appendChild(Statistics.setupUI())
    resultsBox.appendChild(DataTable.setupUI())

    wholeUi.appendChild(resultsBox)

    wholeUi
  }


  def simulate() : Unit = {
    val model = new NagelSchreckenberg.RoadSimulator()
      .createSimulation(Stream.continually(Random.nextDouble),
        DensityBox.getValue,
        ProbabilityBox.getValue,
        SpeedBox.getValue.toInt,
        SpeedStepBox.getValue.toInt,
        Road.cellCountPerRow * WidthBox.getValue.toInt)
    scheduleNextStep(model)
  }

  def scheduleNextStep(iteration: NagelSchreckenberg.Iteration) : Unit = {
    timeout = setTimeout(MinimumTimeoutBox.getValue) {
      iterationNumber = iterationNumber + 1
      Road.draw(iteration.roadState)
      fillStats(iteration.roadState)
      val state = iteration.next()
      scheduleNextStep(state)
    }
  }

  def fillStats(road: Vector[Option[Int]]) = {
    val nonempty = road.filter(p => p.isDefined)
    val avgSpeed = nonempty.foldLeft(0.0)((a,b) => a + b.get) / nonempty.length
    val dev = Math.sqrt(nonempty.foldLeft(0.0)((a,b) => Math.pow(b.get - avgSpeed, 2) + a) / nonempty.length)

    Statistics.setIteration(iterationNumber)
    Statistics.setCount(nonempty.length, road.length)
    Statistics.setAvgSpeed(avgSpeed)
    Statistics.setStdDev(dev)

    if(iterationNumber <= CountedIterationsBox.getValue) {
      DataTable.addDataRow(iterationNumber, avgSpeed, dev)
    }
  }
}