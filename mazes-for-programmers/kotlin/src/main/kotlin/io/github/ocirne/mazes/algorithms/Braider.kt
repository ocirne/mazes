package io.github.ocirne.mazes.algorithms

import io.github.ocirne.mazes.grids.Grid
import kotlin.random.Random.Default.nextDouble

class Braider {

    fun braid(grid: Grid, p:Double=1.0) {
        val deadEnds = grid.deadEnds()
        for (cell in deadEnds.shuffled()) {
            // TODO Es werden mehr als p % DeadEnds entfernt - fixen
            if (cell.links().size != 1 || nextDouble() > p) {
                continue
            }
            val linked = cell.neighbors().filterNot { it.isLinked(cell) }
            var best = linked.filter { it.links().size == 1 }
            if (best.isEmpty()) {
                best = linked
            }
            val neighbor = best.random()
            cell.link(neighbor)
        }
    }
}
