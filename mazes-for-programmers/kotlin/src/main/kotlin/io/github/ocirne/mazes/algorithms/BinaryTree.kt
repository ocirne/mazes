package io.github.ocirne.mazes.algorithms

import io.github.ocirne.mazes.grids.*

// TODO lots duplication
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

    fun on(grid: PolarGrid) {
        for (cell in grid.eachCell()) {
            val neighbors = listOfNotNull(cell.inward, cell.cw)
            if (neighbors.isEmpty()) {
                continue
            }
            val neighbor = neighbors.random()
            cell.link(neighbor)
        }
    }

    fun on(grid: TriangleGrid) {
        for (cell in grid.eachCell()) {
            val neighbors =
                if (cell.isUpright()) {
                    if (cell.east != null) {
                        listOfNotNull(cell.east)
                    } else {
                        listOfNotNull(cell.west)
                    }
                } else {
                    if (cell.east != null && cell.east!!.east == null) {
                        listOfNotNull(cell.north)
                    } else {
                        listOfNotNull(cell.north, cell.east)
                    }
                }
            if (neighbors.isEmpty()) {
                continue
            }
            val neighbor = neighbors.random()
            cell.link(neighbor)
        }

    }

    fun on(grid: HexGrid) {
        for (cell in grid.eachCell()) {
            val neighbors = listOfNotNull(cell.north, cell.northeast, cell.southeast)
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