package io.github.ocirne.mazes.algorithms

import io.github.ocirne.mazes.grids.Cell
import io.github.ocirne.mazes.grids.Grid

class GrowingTree {

    companion object {
        fun <C: Cell> on(grid: Grid<C>, f : (List<Cell>) -> Cell, startAt: C = grid.randomCell()) {
            val active = mutableListOf(startAt)
            while (active.isNotEmpty()) {
                val cell: Cell = f.invoke(active)
                val availableNeighbors = cell.neighbors().filter { it.links().isEmpty() }
                if (availableNeighbors.isNotEmpty()) {
                    val neighbor = availableNeighbors.random()
                    cell.link(neighbor)
                    active.add(neighbor as C)
                } else {
                    active.remove(cell)
                }
            }
        }
    }
}
