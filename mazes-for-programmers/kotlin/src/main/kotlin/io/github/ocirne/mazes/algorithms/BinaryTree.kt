package io.github.ocirne.mazes.algorithms

import io.github.ocirne.mazes.grids.CartesianGrid
import io.github.ocirne.mazes.grids.UpsilonGrid

class BinaryTree : PassageCarver {

    override fun on(grid: CartesianGrid) {
        for (cell in grid.eachCell()) {
            val neighbors = listOfNotNull(cell.north, cell.east)
            if (neighbors.isEmpty()) {
                continue
            }
            val neighbor = neighbors.random()
            cell.link(neighbor)
        }
    }

    fun on(grid: UpsilonGrid) {
        for (cell in grid.eachCell()) {
            val neighbors = listOfNotNull(cell.north, cell.northeast, cell.east, cell.southeast)
            if (neighbors.isEmpty()) {
                continue
            }
            val neighbor = neighbors.random()
            cell.link(neighbor)
        }
    }
}