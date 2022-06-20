package io.github.ocirne.mazes.algorithms

import io.github.ocirne.mazes.grids.Cell
import io.github.ocirne.mazes.grids.Grid

class HuntAndKill {

    companion object {
        fun on(grid: Grid) {
            var current: Cell? = grid.randomCell()
            while (current != null) {
                val unvisitedNeighbors = current.neighbors().filter { it.links().isEmpty() }
                current = if (unvisitedNeighbors.isNotEmpty()) {
                    val neighbor = unvisitedNeighbors.random()
                    current.link(neighbor)
                    neighbor
                } else {
                    null
                }
                for (cell in grid.eachCell()) {
                    val visitedNeighbors = cell.neighbors().filter { it.links().isNotEmpty() }
                    if (cell.links().isEmpty() && visitedNeighbors.isNotEmpty()) {
                        current = cell
                        val neighbor = visitedNeighbors.random()
                        current.link(neighbor)
                    }
                }
            }
        }
    }
}
