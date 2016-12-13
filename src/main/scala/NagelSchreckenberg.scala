object NagelSchreckenberg {
  type Speed = Int
  type Vehicle = Speed

  class Configuration(maxSpeed: Speed, speedIncrement: Speed, random: Stream[Double]) {
    var randoms = random

    def getMaxSpeed: Speed = maxSpeed
    def getSpeedIncrement: Speed = speedIncrement
    def nextRandom: Double = {
      val next = randoms.headOption
      randoms = randoms drop 1
      next.get
    }
  }

  class Iteration(road: Vector[Option[Vehicle]], config: Configuration) {

    def speedUp(vehicle: Vehicle): Vehicle =
      if (vehicle >= config.getMaxSpeed) config.getMaxSpeed else vehicle + config.getSpeedIncrement
    def accelerate(vehicle: Option[Vehicle]): Option[Vehicle] = vehicle map speedUp

    def slowDown(index: Int)(vehicle: Vehicle): Vehicle = {
      val accessed = (1 until vehicle).filter(i => !road(index + i).isEmpty)
      if (accessed.isEmpty) vehicle else vehicle - accessed.head
    }
    def slowdown(vehicle: (Option[Vehicle], Int)): (Option[Vehicle], Int) = (vehicle._1 map slowDown(vehicle._2), vehicle._2)

    def randomSlowdown(vehicle: Vehicle): Vehicle = if (config.nextRandom > 0.5) vehicle else Math.max(0, vehicle - 1)
    def randomize(vehicle: (Option[Vehicle], Int)): (Option[Vehicle], Int) = (vehicle._1 map randomSlowdown, vehicle._2)

    def move(vehicles: Vector[(Option[Vehicle], Int)]): Vector[Option[Vehicle]] = {
      vehicles.foldLeft(Vector.fill(road.length)(None) : Vector[Option[Vehicle]]) {
        (newRoad, vehicle) =>
          if(vehicle._1.isEmpty) newRoad
          else newRoad.updated((vehicle._2 + vehicle._1.get) % newRoad.length, vehicle._1)
      }
    }


    def next(): Iteration = {
      val accelerated = (road map accelerate).zipWithIndex

      new Iteration(move
      (accelerated
        map slowdown
        map randomize)
      , config)
    }
  }

  class RoadSimulator {
    def createSimulation(random: => Stream[Double], width: Int): Iteration = {
      val initialState = random take width map (_ => None)
      new Iteration(initialState toVector, new Configuration(5, 1, random))
    }
  }
}
