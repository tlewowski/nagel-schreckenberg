package own

import scala.scalajs.js.JSApp
import org.scalajs.dom._
import own.controllers.NagelSchreckenbergController

object WebApp extends JSApp{
  def main(): Unit = {
    document.getElementById("main-display").appendChild(NagelSchreckenbergController.setupUI())
  }
}
