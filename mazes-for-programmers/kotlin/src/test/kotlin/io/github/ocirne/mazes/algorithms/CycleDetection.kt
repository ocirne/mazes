package io.github.ocirne.mazes.algorithms

import io.github.ocirne.mazes.grids.Cell
import io.github.ocirne.mazes.grids.Grid
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.shouldBe

/** see https://en.wikipedia.org/wiki/Cycle_(graph_theory) */
class CycleDetection(private val grid: Grid) {
    private val visited: MutableSet<Cell> = mutableSetOf()
    private val finished: MutableSet<Cell> = mutableSetOf()

    fun assertNoCycle() {
        // TODO für sparse grids schon mal "noNeighborsAllowed = false" eingefügt, aber ...
        val cell = grid.randomCell(noNeighborsAllowed = false)
        dfs(cell, cell)
        // all cells are visited - that also means not every cell must be the start
        // TODO das ist dann falsch
        visited.size shouldBe grid.size()
    }

    private fun dfs(v: Cell, before: Cell) {
        if (finished.contains(v)) {
            return
        }
        // no cell is visited twice
        visited shouldNotContain v
        visited.add(v)
        for (w in v.links().filterNot { it == before }) {
            dfs(w, v)
        }
        finished.add(v)
    }
}
