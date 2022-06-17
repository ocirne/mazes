package io.github.ocirne.mazes.algorithms

import io.github.ocirne.mazes.grids.CartesianGrid

class AldousBroder {

    companion object {
        fun on(grid: CartesianGrid) {
            var cell = grid.randomCell()
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
}
