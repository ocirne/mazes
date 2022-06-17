package io.github.ocirne.mazes.algorithms

import io.github.ocirne.mazes.grids.CartesianGrid

class BinaryTree {

    companion object {
        fun on(grid: CartesianGrid) {
            for (cell in grid.eachCell()) {
                val neighbors = listOfNotNull(cell.north, cell.east)
                if (neighbors.isEmpty()) {
                    continue
                }
                val neighbor = neighbors.random()
                cell.link(neighbor)
            }
        }
    }
}