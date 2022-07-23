package io.github.ocirne.mazes.algorithms

import io.github.ocirne.mazes.grids.Cell
import io.github.ocirne.mazes.grids.GridProvider
import io.github.ocirne.mazes.grids.Maze

class GrowingTree(private val f : (List<Cell>) -> Cell):  PassageCarver {

    override fun on(gridProvider: GridProvider, startAt: (Maze) -> Cell): Maze {
        val grid = gridProvider.forPassageCarver()
        val active = mutableListOf(startAt.invoke(grid))
        while (active.isNotEmpty()) {
            val cell: Cell = f.invoke(active)
            val availableNeighbors = cell.neighbors().filter { it.links().isEmpty() }
            if (availableNeighbors.isNotEmpty()) {
                val neighbor = availableNeighbors.random()
                cell.link(neighbor)
                active.add(neighbor)
            } else {
                active.remove(cell)
            }
        }
        return grid
    }
}
