package io.github.ocirne.mazes.algorithms

import io.github.ocirne.mazes.grids.Cell
import io.github.ocirne.mazes.grids.MutableGrid

class AldousBroder : PassageCarver {

    override fun on(grid: MutableGrid, startAt: Cell) {
        var cell = startAt
        var unvisited = grid.size() - 1

        while (unvisited > 0) {
            val neighbor = cell.neighbors().random()
            if (neighbor.links().isEmpty()) {
                cell.link(neighbor)
                unvisited -= 1
            }
            cell = neighbor
        }
    }
}
