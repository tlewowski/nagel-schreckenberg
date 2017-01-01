package ui

import org.scalajs.dom.html.{Div, Element}
import org.scalajs.dom.{CanvasRenderingContext2D, document, html}
import ui.helper.HtmlCreator

object Road {
  val cellCountPerRow = 150
  val cellSizeInPixels = 5

  def setupUI(): Element = {

    val roadBox = HtmlCreator.create[Div]("simulation-box")

    val simulationCanvas = document.createElement("canvas")
    simulationCanvas.id = "simulation-canvas"
    simulationCanvas.setAttribute("width", cellCountPerRow * cellSizeInPixels + "px")

    roadBox.appendChild(simulationCanvas)

    roadBox
  }

  def draw(road: Vector[Option[Int]]): Unit = {
    val rows = road.length / cellCountPerRow
    val canvas = document.getElementById("simulation-canvas").asInstanceOf[html.Canvas]
    val ctx = canvas.getContext("2d").asInstanceOf[CanvasRenderingContext2D]
    canvas.width = cellCountPerRow * cellSizeInPixels
    canvas.height = rows * cellSizeInPixels
    ctx.strokeStyle = "red"
    ctx.lineWidth = 20

    road.zipWithIndex.foreach(r => {
      if (r._1.isDefined) {
        ctx.fillRect(cellSizeInPixels*(r._2 % cellCountPerRow), cellSizeInPixels*(r._2 / cellCountPerRow), cellSizeInPixels, cellSizeInPixels)
      }
      else {
        ctx.clearRect(cellSizeInPixels*(r._2 % cellCountPerRow), cellSizeInPixels*(r._2 / cellCountPerRow), cellSizeInPixels, cellSizeInPixels)
      }
    })

    ctx.stroke
  }
}
