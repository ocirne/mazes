package io.github.ocirne.mazes.algorithms

import io.github.ocirne.mazes.grids.Cell
import io.github.ocirne.mazes.grids.Grid
import io.github.ocirne.mazes.output.saveImage
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.shouldBe

/** see https://en.wikipedia.org/wiki/Cycle_(graph_theory) */
class CycleDetection(private val grid: Grid<Cell>) {
    private val visited: MutableSet<Cell> = mutableSetOf()
    private val finished: MutableSet<Cell> = mutableSetOf()

    fun detect() {
        for (cell in grid.eachCell()) {
            dfs(cell, cell)
        }
        // all cells are visited
        visited.size shouldBe grid.size()
    }

    private fun dfs(v: Cell, b: Cell) {
        if (finished.contains(v)) {
            return
        }
        if (visited.contains(v)) {
            saveImage(grid.toImage(), filename = "test")
        }
        visited shouldNotContain v
        visited.add(v)
        for (w in v.links()) {
            if (w == b) {
                continue
            }
            dfs(w, v)
        }
        finished.add(v)
    }
}
