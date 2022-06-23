package io.github.ocirne.mazes.algorithms

import io.github.ocirne.mazes.grids.Grid
import kotlin.random.Random

class DeadEndKiller {

    fun remove(grid: Grid, passes:Int=1, p:Double=1.0) {
        repeat(passes) {
            val deadEnds = grid.deadEnds()
            for (cell in deadEnds.shuffled()) {
                if (cell.links().size != 1 || Random.nextDouble() > p) {
                    continue
                }
                cell.neighbors().map { it.unlink(cell) }
            }
        }
    }
}