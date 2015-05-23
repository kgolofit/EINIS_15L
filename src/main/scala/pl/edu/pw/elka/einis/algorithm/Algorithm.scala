package pl.edu.pw.elka.einis.algorithm

import java.util
import java.util.Random

import org.uncommons.maths.random.JavaRNG
import org.uncommons.watchmaker.framework._
import org.uncommons.watchmaker.framework.factories.AbstractCandidateFactory
import org.uncommons.watchmaker.framework.operators.{EvolutionPipeline, DoubleArrayCrossover}
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
  var progressListener: ProgressListener = null

  /**
   * Rozwiązuje problem
   *
   * @param input
   * @param params parametry
   * @return znalezione rozwiązanie
   */
  def solve(input: util.List[Point], params: AlgorithmParameters): Polynomial = solve(input.toList, params)

  /**
   * Rozwiązuje problem
   *
   * @param input
   * @param params parametry
   * @return
   */
  def solve(input: Input, params: AlgorithmParameters): Polynomial = {
    val engine = createEngine(input, params.polynomialDegree)
    if (progressListener != null) {
      engine.addEvolutionObserver(new EvolutionObserver[Polynomial] {
        override def populationUpdate(populationData: PopulationData[_ <: Polynomial]): Unit =
          progressListener.update(populationData.getGenerationNumber.toDouble / params.iterationNum)
      })
    }
    engine.evolve(params.populationSize, params.successorsNum, new GenerationCount(params.iterationNum))
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
          random.nextDouble() * 10 - 5
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
    val crossover = new EvolutionaryOperator[Polynomial] {
      override def apply(list: util.List[Polynomial], random: Random): util.List[Polynomial] = {
        // Wykorzystujemy DoubleArrayCrossover na współczynnikach wielomianu
        val newCoeffsList = new DoubleArrayCrossover().apply(list.toList.map(polynomial => polynomial.coeffs), random)
        newCoeffsList.map(coeffs => new Polynomial(coeffs))
      }
    }
    val mutation = new EvolutionaryOperator[Polynomial] {
      override def apply(list: util.List[Polynomial], random: Random): util.List[Polynomial] = {
        if (random.nextDouble() > 0.5) { //prawd. mutacji
          val newList = new util.ArrayList[Polynomial]
          for (polynomial <- list) {
            val newCoeffs = polynomial.coeffs.map(coeff => {
              val mutation = random.nextGaussian()
              coeff + mutation
            })
            newList.add(new Polynomial(newCoeffs))
          }
          return newList
        }
        list
      }
    }
    val pipeline = new EvolutionPipeline[Polynomial](util.Arrays.asList(crossover, mutation))
    // TODO:: Selection strategy - do ogarnięcia, która jak działa
    val ss = new RankSelection()
    new GenerationalEvolutionEngine[Polynomial](cf, pipeline, fe, ss, new JavaRNG())
  }
}
