package pl.edu.pw.elka.einis.algorithm

import pl.edu.pw.elka.einis.entity.Point

/**
 * Klasa X
 *
 * Data utworzenia: 23.05.15 15:09
 * @author Michał Toporowski
 */
class AlgorithmTest extends org.scalatest.FunSuite {

  test("evolving test") {
    val points = List(new Point(2, 3), new Point(0, 1), new Point(1.5, 0.9))
    val algorithm = new Algorithm
    val result = algorithm.solve(points, new AlgorithmParameters(5, 100, 50, 100))
    assert(result != null)
    println(result)
  }
}
