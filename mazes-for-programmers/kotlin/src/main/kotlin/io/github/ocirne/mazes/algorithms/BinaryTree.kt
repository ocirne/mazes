package io.github.ocirne.mazes.algorithms

import io.github.ocirne.mazes.grids.*
import io.github.ocirne.mazes.grids.cartesian.CartesianGrid.CartesianMaze
import io.github.ocirne.mazes.grids.hex.HexGrid.HexMaze
import io.github.ocirne.mazes.grids.polar.PolarGrid.PolarMaze
import io.github.ocirne.mazes.grids.triangle.TriangleGrid.TriangleMaze
import io.github.ocirne.mazes.grids.upsilon.UpsilonGrid.UpsilonMaze
import io.github.ocirne.mazes.grids.cartesian.CartesianGrid
import io.github.ocirne.mazes.grids.hex.HexGrid
import io.github.ocirne.mazes.grids.polar.PolarGrid
import io.github.ocirne.mazes.grids.triangle.TriangleGrid
import io.github.ocirne.mazes.grids.upsilon.UpsilonGrid

class BinaryTree : PassageCarver {

    override fun on(gridProvider: GridProvider, startAt: (Maze) -> Cell): Maze {
        // sieht doof aus, aber funktioniert
        return when (val grid = gridProvider.forPassageCarver()) {
            is CartesianMaze -> on(grid)
            is PolarMaze -> on(grid)
            is TriangleMaze -> on(grid)
            is HexMaze -> on(grid)
            is UpsilonMaze -> on(grid)
            else -> throw NotImplementedError()
        }
    }

    private fun generalizedBinaryTree(grid: Maze, availableNeighbors: (cell: Cell) -> List<Cell>): Maze {
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

    fun on(grid: CartesianMaze): Maze {
        return generalizedBinaryTree(grid) { cell -> listOfNotNull((cell as CartesianGrid.CartesianCell).north, cell.east) }
    }

    fun on(grid: PolarMaze): Maze {
        return generalizedBinaryTree(grid) { cell -> listOfNotNull((cell as PolarGrid.PolarCell).inward, cell.cw) }
    }

    fun on(grid: TriangleMaze): Maze {
        return generalizedBinaryTree(grid) { cell ->
            cell as TriangleGrid.TriangleCell
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

    fun on(grid: HexMaze): Maze {
        return generalizedBinaryTree(grid) { cell -> listOfNotNull((cell as HexGrid.HexCell).north, cell.northeast, cell.southeast) }
    }

    fun on(grid: UpsilonMaze): Maze {
        return generalizedBinaryTree(grid) { cell -> listOfNotNull((cell as UpsilonGrid.UpsilonCell).north, cell.northeast, cell.east, cell.southeast) }
    }
}