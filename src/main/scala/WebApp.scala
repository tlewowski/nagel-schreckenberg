import scala.scalajs.js.JSApp
import org.scalajs.dom._

object WebApp extends JSApp{
  def main(): Unit = {
    document.getElementById("main-display").appendChild(NagelSchreckenbergController.setupUI())
  }
}
