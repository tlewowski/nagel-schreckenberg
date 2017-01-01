package ui

object WidthBox extends LabeledBox {
  override def getId = "width"
  override def getLabel = "Liczba wierszy jezdni (150 komórek na wiersz)"
  override def getMin: Double = 1
  override def getMax: Double = 1000
  override def getStep: Double = 1
  override def getDefault: Double = 150
}

object DensityBox extends LabeledBox {
  override def getId = "density"
  override def getLabel = "Gęstość pojazdów"
  override def getMin: Double = 0
  override def getMax: Double = 1
  override def getStep: Double = 0.0001
  override def getDefault: Double = 0.1
}

object ProbabilityBox extends LabeledBox {
  override def getId = "probability"
  override def getLabel = "Prawdopodobieństwo spowolnienia"
  override def getMin: Double = 0
  override def getMax: Double = 1
  override def getStep: Double = 0.0001
  override def getDefault: Double = 0.2
}

object SpeedBox extends LabeledBox {
  override def getId: String = "max-speed"
  override def getLabel: String = "Maksymalna prędkość"
  override def getMin: Double = 1
  override def getMax: Double = 140
  override def getStep: Double = 1
  override def getDefault: Double = 5
}

object SpeedStepBox extends LabeledBox {
  override def getId: String = "speed-step"
  override def getLabel: String = "Krok zmiany prędkości"
  override def getMin: Double = 1
  override def getMax: Double = SpeedBox.getValue
  override def getStep: Double = 1
  override def getDefault: Double = 1
}

object MinimumTimeoutBox extends LabeledBox {
  override def getId: String = "minimum-timeout"
  override def getLabel: String = "Minimalne opóźnienie między kolejnymi ramkami (ms)"
  override def getMin: Double = 0
  override def getMax: Double = 10000
  override def getStep: Double = 1
  override def getDefault: Double = 0
}

object CountedIterationsBox extends LabeledBox {
  override def getId: String = "counted-iterations"
  override def getLabel: String = "Liczba iteracji do zapisania w tabeli"
  override def getMin: Double = 0
  override def getMax: Double = 10000
  override def getStep: Double = 1
  override def getDefault: Double = 150
}
