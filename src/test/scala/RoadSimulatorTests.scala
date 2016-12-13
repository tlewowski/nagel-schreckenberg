import org.scalatest._

/**
  * Created by Ja on 12.12.2016.

class RoadSimulatorTests extends FlatSpec with Matchers {
  "Newly created simulator" should "be equal to itself" in {
    val s = new NagelSchreckenberg.RoadSimulator(1)
    s should be (s)
  }

  "Newly created simulator" should "be different than another one" in {
    new NagelSchreckenberg.RoadSimulator(1) should not be new NagelSchreckenberg.RoadSimulator(1)
  }

  it should "take model width as parameter" in {
    new NagelSchreckenberg.RoadSimulator(1000)
  }

}
*/