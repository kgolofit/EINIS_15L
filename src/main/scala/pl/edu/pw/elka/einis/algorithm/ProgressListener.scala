package pl.edu.pw.elka.einis.algorithm

/**
 * Słuchacz postępu wykonywania algorytmu
 *
 * Data utworzenia: 23.05.15 17:09
 * @author Michał Toporowski
 */
trait ProgressListener {
  /**
   * Aktualizuje postęp
   *
   * @param progress postęp w procentach
   */
  def update(progress: Double)
}
