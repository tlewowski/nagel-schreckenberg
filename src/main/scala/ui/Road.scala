package ui

import org.scalajs.dom.html.{Div, Element}
import org.scalajs.dom.{CanvasRenderingContext2D, document, html}
import ui.helper.HtmlCreator

object Road {
  val cellCountPerRow = 150

  def setupUI(): Element = {

    val roadBox = HtmlCreator.create[Div]("simulation-box")

    val simulationCanvas = document.createElement("canvas")
    simulationCanvas.id = "simulation-canvas"
    simulationCanvas.setAttribute("width", "600px")

    roadBox.appendChild(simulationCanvas)

    roadBox
  }

  def draw(road: Vector[Option[Int]]): Unit = {
    val sizeInPixels = 5
    val rows = road.length / cellCountPerRow
    val canvas = document.getElementById("simulation-canvas").asInstanceOf[html.Canvas]
    val ctx = canvas.getContext("2d").asInstanceOf[CanvasRenderingContext2D]
    canvas.width = cellCountPerRow * sizeInPixels
    canvas.height = rows * sizeInPixels
    ctx.strokeStyle = "red"
    ctx.lineWidth = 20

    road.zipWithIndex.foreach(r => {
      if (r._1.isDefined) {
        ctx.fillRect(sizeInPixels*(r._2 % cellCountPerRow), sizeInPixels*(r._2 / cellCountPerRow), sizeInPixels, sizeInPixels)
      }
      else {
        ctx.clearRect(sizeInPixels*(r._2 % cellCountPerRow), sizeInPixels*(r._2 / cellCountPerRow), sizeInPixels, sizeInPixels)
      }
    })

    ctx.stroke
  }
}
