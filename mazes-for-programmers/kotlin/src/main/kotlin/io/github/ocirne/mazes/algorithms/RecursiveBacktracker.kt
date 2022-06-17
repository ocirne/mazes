package io.github.ocirne.mazes.algorithms

import io.github.ocirne.mazes.grids.Cell
import io.github.ocirne.mazes.grids.Grid

class RecursiveBacktracker {

    companion object {
        fun <C: Cell> on(grid: Grid<C>, startAt: C = grid.randomCell()): Grid<C> {
            val stack = ArrayDeque<C>()
            stack.addLast(startAt)
            while (stack.isNotEmpty()) {
                val current = stack.last()
                val neighbors = current.neighbors().filter { it.links.isEmpty() }

                if (neighbors.isEmpty()) {
                    stack.removeLast()
                } else {
                    val neighbor = neighbors.random()
                    current.link(neighbor)
                    stack.addLast(neighbor as C)
                }
            }
            return grid
        }
    }

    fun recursivelyOn(grid: Grid<Cell>, start_at: Cell = grid.randomCell()): Grid<Cell> {
        walkFrom(start_at)
        return grid
    }

    private fun walkFrom(cell: Cell) {
        for (neighbor in cell.neighbors().shuffled()) {
            if (neighbor.links.isEmpty()) {
                cell.link(neighbor)
                walkFrom(neighbor)
            }
        }
    }
}
