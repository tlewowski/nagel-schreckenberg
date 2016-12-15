package own.models

object NagelSchreckenberg {
  type Speed = Int
  type Vehicle = Speed

  class Configuration(maxSpeed: Speed, speedIncrement: Speed, slowdownProbability: Double, random: Stream[Double]) {

    var randoms = random

    def getMaxSpeed: Speed = maxSpeed
    def getSlowdownProbability: Double = slowdownProbability
    def getSpeedIncrement: Speed = speedIncrement
    def nextRandom: Double = {
      val next = randoms.headOption
      randoms = randoms drop 1
      next.get
    }
  }

  class Iteration(road: Vector[Option[Vehicle]], config: Configuration) {
    def roadState = road

    def speedUp(vehicle: Vehicle): Vehicle =
      if (vehicle >= config.getMaxSpeed) config.getMaxSpeed else vehicle + config.getSpeedIncrement
    def accelerate(vehicle: Option[Vehicle]): Option[Vehicle] = vehicle map speedUp

    def slowDown(index: Int)(vehicle: Vehicle): Vehicle = {
      val accessed = (1 until (vehicle + 1)).filter(i => road((index + i) % road.size).isDefined)
      if (accessed.isEmpty) vehicle else accessed.head - 1
    }
    def slowdown(vehicle: (Option[Vehicle], Int)): (Option[Vehicle], Int) = (vehicle._1 map slowDown(vehicle._2), vehicle._2)

    def randomSlowdown(vehicle: Vehicle): Vehicle = if (config.nextRandom > config.getSlowdownProbability) vehicle else Math.max(0, vehicle - config.getSpeedIncrement)
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
      val random = accelerated map randomize
      val slowed = random map slowdown
      val moved = move(slowed)

      new Iteration(moved, config)
    }
  }

  class RoadSimulator {
    def createSimulation(random: => Stream[Double],
                         vehicleDensity: Double,
                         slowdownProbability: Double,
                         maxSpeed: Int,
                         slowdownStep: Int,
                         width: Int): Iteration = {
      assert(slowdownProbability >= 0 && slowdownProbability <= 1)
      assert(vehicleDensity >= 0 && vehicleDensity <= 1)
      assert(slowdownStep <= maxSpeed)

      val initialState = random take width map (x => if(x < vehicleDensity) Some(((x / vehicleDensity) * (maxSpeed + 1)).floor.toInt) else None)
      new Iteration(initialState toVector, new Configuration(maxSpeed, slowdownStep, slowdownProbability, random))
    }
  }
}
