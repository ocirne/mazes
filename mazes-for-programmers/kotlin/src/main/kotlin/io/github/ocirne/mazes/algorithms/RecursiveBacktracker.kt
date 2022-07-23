package io.github.ocirne.mazes.algorithms

import io.github.ocirne.mazes.grids.Cell
import io.github.ocirne.mazes.grids.GridProvider
import io.github.ocirne.mazes.grids.Maze

class RecursiveBacktracker : PassageCarver {

    override fun on(gridProvider: GridProvider, startAt: (Maze) -> Cell): Maze {
        val grid = gridProvider.forPassageCarver()
        val stack = ArrayDeque<Cell>()
        stack.addLast(startAt.invoke(grid))
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
        return grid
    }

    fun recursivelyOn(grid: Maze, start_at: Cell = grid.randomCell()): Maze {
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
