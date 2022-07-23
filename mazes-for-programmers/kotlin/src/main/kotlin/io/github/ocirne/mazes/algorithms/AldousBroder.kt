package io.github.ocirne.mazes.algorithms

import io.github.ocirne.mazes.grids.Cell
import io.github.ocirne.mazes.grids.GridProvider
import io.github.ocirne.mazes.grids.Maze

class AldousBroder : PassageCarver {

    override fun on(gridProvider: GridProvider, startAt: (Maze) -> Cell): Maze {
        val grid = gridProvider.forPassageCarver()
        var cell = startAt.invoke(grid)
        var unvisited = grid.size() - 1

        while (unvisited > 0) {
            val neighbor = cell.neighbors().random()
            if (neighbor.links().isEmpty()) {
                cell.link(neighbor)
                unvisited -= 1
            }
            cell = neighbor
        }
        return grid
    }
}
