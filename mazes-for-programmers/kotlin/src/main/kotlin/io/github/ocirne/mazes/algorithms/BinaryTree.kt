package io.github.ocirne.mazes.algorithms

import io.github.ocirne.mazes.grids.*

class BinaryTree : PassageCarver {

    override fun on(gridProvider: GridProvider, startAt: (MutableGrid) -> Cell): MutableGrid {
        // sieht doof aus, aber funktioniert
        return when (val grid = gridProvider.createPassageCarver()) {
            is CartesianGrid -> on(grid)
            is PolarGrid -> on(grid)
            is TriangleGrid -> on(grid)
            is HexGrid -> on(grid)
            is UpsilonGrid -> on(grid)
            else -> throw NotImplementedError()
        }
    }

    private fun generalizedBinaryTree(grid: MutableGrid, availableNeighbors: (cell: Cell) -> List<Cell>): MutableGrid {
        for (cell in grid.eachCell()) {
            val neighbors = availableNeighbors(cell)
            if (neighbors.isEmpty()) {
                continue
            }
            val neighbor = neighbors.random()
            cell.link(neighbor)
        }
        return grid
    }

    fun on(grid: CartesianGrid): MutableGrid {
        return generalizedBinaryTree(grid) { cell -> listOfNotNull((cell as CartesianCell).north, cell.east) }
    }

    fun on(grid: PolarGrid): MutableGrid {
        return generalizedBinaryTree(grid) { cell -> listOfNotNull((cell as PolarCell).inward, cell.cw) }
    }

    fun on(grid: TriangleGrid): MutableGrid {
        return generalizedBinaryTree(grid) { cell ->
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

    fun on(grid: HexGrid): MutableGrid {
        return generalizedBinaryTree(grid) { cell -> listOfNotNull((cell as HexCell).north, cell.northeast, cell.southeast) }
    }

    fun on(grid: UpsilonGrid): MutableGrid {
        return generalizedBinaryTree(grid) { cell -> listOfNotNull((cell as UpsilonCell).north, cell.northeast, cell.east, cell.southeast) }
    }
}