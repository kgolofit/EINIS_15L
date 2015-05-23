package pl.edu.pw.elka.einis.entity

/**
 * Wielomian
 *
 * Data utworzenia: 23.05.15 12:44
 *
 *
 * @param coeffs: współczynniki wielomianu (od 0 do n)
 * @author Michał Toporowski
 */
class Polynomial(val coeffs: Array[Double]) {

  /**
   * Oblicza wartość wielomianu dla x
   *
   * @param x x
   * @return wartość wielomianu
   */
  def calculate(x: Double) = {
    var result = 0.0
    coeffs.indices.foreach(i => {
      result += coeffs(i) * math.pow(x, i)
    })
    result
  }
}
