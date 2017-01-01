package ui

trait SimulationTrigger {
  def revalidate(): Unit
  def startSimulation(): Unit
  def stopSimulation(): Unit
}
