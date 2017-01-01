package ui

import org.scalajs.dom.{Element, document}

object DataTable {
  def setupUI(): Element = {
    val dataBox = document.createElement("div")
    dataBox.id = "data-box"
    dataBox.appendChild(setupHeader)

    dataBox
  }

  def clear(): Unit = {
    val dataBox = document.getElementById("data-box")
    dataBox.removeChild(document.getElementById("data-table"))
    dataBox.appendChild(setupUI)
  }

  def addDataRow(iterationNumber: Int, avgSpeed: Double, dev: Double): Unit = {
    val table = document.getElementById("data-table-body")
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

  private def setupHeader = {
    val dataTable = document.createElement("table")
    dataTable.id = "data-table"
    dataTable.classList.add("table")
    dataTable.classList.add("table-striped")

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

    val body = document.createElement("tbody")
    body.id = "data-table-body"
    dataTable.appendChild(body)

    dataTable
  }
}
