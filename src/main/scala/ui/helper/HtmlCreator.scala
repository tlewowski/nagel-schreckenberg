package ui.helper

import org.scalajs.dom.document
import org.scalajs.dom.html.Element

object HtmlCreator {
  def create[T <: Element](id: String): T = {
    val div = document.createElement("div").asInstanceOf[T]
    div.id = id

    div
  }
}
