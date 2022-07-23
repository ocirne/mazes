package io.github.ocirne.mazes.algorithms

import io.github.ocirne.mazes.grids.*

class BinaryTree : PassageCarver {

    override fun on(grid: MutableGrid, startAt: Cell) {
        // sieht doof aus, aber funktioniert
        return when (grid) {
            is CartesianGrid -> on(grid)
            is PolarGrid -> on(grid)
            is TriangleGrid -> on(grid)
            is HexGrid -> on(grid)
            is UpsilonGrid -> on(grid)
            else -> throw NotImplementedError()
        }
    }

    private fun generalizedBinaryTree(grid: MutableGrid, availableNeighbors: (cell: Cell) -> List<Cell>) {
        for (cell in grid.eachCell()) {
            val neighbors = availableNeighbors(cell)
            if (neighbors.isEmpty()) {
                continue
            }
            val neighbor = neighbors.random()
            cell.link(neighbor)
        }
    }

    fun on(grid: CartesianGrid) {
        generalizedBinaryTree(grid) { cell -> listOfNotNull((cell as CartesianCell).north, cell.east) }
    }

    fun on(grid: PolarGrid) {
        generalizedBinaryTree(grid) { cell -> listOfNotNull((cell as PolarCell).inward, cell.cw) }
    }

    fun on(grid: TriangleGrid) {
        generalizedBinaryTree(grid) { cell ->
            cell as TriangleCell
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
        }
    }

    fun on(grid: HexGrid) {
        generalizedBinaryTree(grid) { cell -> listOfNotNull((cell as HexCell).north, cell.northeast, cell.southeast) }
    }

    fun on(grid: UpsilonGrid) {
        generalizedBinaryTree(grid) { cell -> listOfNotNull((cell as UpsilonCell).north, cell.northeast, cell.east, cell.southeast) }
    }
}