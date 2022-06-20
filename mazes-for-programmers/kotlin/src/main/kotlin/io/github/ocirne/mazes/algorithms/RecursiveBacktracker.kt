package io.github.ocirne.mazes.algorithms

import io.github.ocirne.mazes.grids.Cell
import io.github.ocirne.mazes.grids.Grid

class RecursiveBacktracker {

    companion object {
        fun on(grid: Grid, startAt: Cell = grid.randomCell()) {
            val stack = ArrayDeque<Cell>()
            stack.addLast(startAt)
            while (stack.isNotEmpty()) {
                val current = stack.last()
                val neighbors = current.neighbors().filter { it.links().isEmpty() }

                if (neighbors.isEmpty()) {
                    stack.removeLast()
                } else {
                    val neighbor = neighbors.random()
                    current.link(neighbor)
                    stack.addLast(neighbor)
                }
            }
        }
    }

    fun recursivelyOn(grid: Grid, start_at: Cell = grid.randomCell()): Grid {
        walkFrom(start_at)
        return grid
    }

    private fun walkFrom(cell: Cell) {
        for (neighbor in cell.neighbors().shuffled()) {
            if (neighbor.links().isEmpty()) {
                cell.link(neighbor)
                walkFrom(neighbor)
            }
        }
    }
}
