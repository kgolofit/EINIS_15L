package pl.edu.pw.elka.einis.algorithm

import java.util
import java.util.Random

import org.uncommons.maths.random.JavaRNG
import org.uncommons.watchmaker.framework._
import org.uncommons.watchmaker.framework.factories.AbstractCandidateFactory
import org.uncommons.watchmaker.framework.operators.DoubleArrayCrossover
import org.uncommons.watchmaker.framework.selection.RankSelection
import org.uncommons.watchmaker.framework.termination.GenerationCount
import pl.edu.pw.elka.einis.entity.{Point, Polynomial}

import scala.collection.JavaConversions._

/**
 * Algorytm obliczający wynik
 *
 * Data utworzenia: 23.05.15 13:42
 * @author Michał Toporowski
 */
class Algorithm {
  type Input = List[Point]

  /**
   * Rozwiązuje problem
   *
   * @param input
   * @param polynomialDegree
   * @return
   */
  def solve(input: Input, polynomialDegree: Int): Polynomial = {
    val engine = createEngine(input, polynomialDegree)
    //FIXME:: hardcode
    //TODO:: można dodać evolution observer
    engine.evolve(100, 50, new GenerationCount(100))
  }

  /**
   * Tworzy silnik AE
   *
   * @param input
   * @param polynomialDegree
   * @return
   */
  private def createEngine(input: Input, polynomialDegree: Int) = {
    // TODO:: może przenieść te rzeczy do osobnych klas
    val cf = new AbstractCandidateFactory[Polynomial] {
      override def generateRandomCandidate(random: Random): Polynomial = {
        val coeffs = new Range(0, polynomialDegree, 1).map(i => {
          random.nextDouble()
        }).toArray
        new Polynomial(coeffs)
      }
    }
    val fe = new FitnessEvaluator[Polynomial] {
      override def getFitness(t: Polynomial, list: util.List[_ <: Polynomial]): Double = {
        // Funkcja celu: Na razie: liczymy wartość funkcji dla każdego z punktów
        // i porównujemy z 'y'
        input.map(point => {
          math.abs(t.calculate(point.x) - point.y)
        }).reduce((f1, f2) => f1 + f2)
      }

      override def isNatural: Boolean = false
    }
    val eo = new EvolutionaryOperator[Polynomial] {
      override def apply(list: util.List[Polynomial], random: Random): util.List[Polynomial] = {
        // Wykorzystujemy DoubleArrayCrossover na współczynnikach wielomianu
        val newCoeffsList = new DoubleArrayCrossover().apply(list.toList.map(polynomial => polynomial.coeffs), random)
        newCoeffsList.map(coeffs => new Polynomial(coeffs))
      }
    }
    // TODO:: Selection strategy - do ogarnięcia, która jak działa
    val ss = new RankSelection()
    new GenerationalEvolutionEngine[Polynomial](cf, eo, fe, ss, new JavaRNG())
  }
}
