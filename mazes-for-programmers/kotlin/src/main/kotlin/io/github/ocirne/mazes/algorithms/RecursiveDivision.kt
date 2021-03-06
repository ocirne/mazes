package io.github.ocirne.mazes.algorithms

import io.github.ocirne.mazes.grids.Cell
import io.github.ocirne.mazes.grids.cartesian.CartesianGrid.CartesianCell
import io.github.ocirne.mazes.grids.cartesian.CartesianGrid.CartesianMaze
import io.github.ocirne.mazes.grids.GridProvider
import io.github.ocirne.mazes.grids.Maze
import kotlin.random.Random.Default.nextInt

class RecursiveDivision(private val rooms: Boolean=false): WallAdder {

    override fun on(gridProvider: GridProvider, startAt: (Maze) -> Cell): CartesianMaze {
        val grid = gridProvider.forWallAdder()
        grid as CartesianMaze
        divide(grid, 0, 0, grid.getRows(), grid.getColumns())
        return grid
    }

    private fun divide(grid: CartesianMaze, row: Int, column: Int, height: Int, width: Int) {
        if (rooms) {
            if (height <= 1 || width <= 1 || (height < 5 && width < 5 && nextInt(4) == 0)) {
                return
            }
        } else {
            if (height <= 1 || width <= 1) {
                return
            }
        }
        if (height > width) {
            divideHorizontally(grid, row, column, height, width)
        } else {
            divideVertically(grid, row, column, height, width)
        }
    }

    private fun divideHorizontally(grid: CartesianMaze, row: Int, column: Int, height: Int, width: Int) {
        val divideSouthOf = nextInt(height - 1)
        val passageAt = nextInt(width)
        repeat(width) { x ->
            if (passageAt != x) {
                val cell = grid[row + divideSouthOf, column + x]!!
                cell.unlink(cell.south as CartesianCell)
            }
        }
        divide(grid, row, column, divideSouthOf+1, width)
        divide(grid, row+divideSouthOf+1, column, height-divideSouthOf-1, width)
    }

    private fun divideVertically(grid: CartesianMaze, row: Int, column: Int, height: Int, width: Int) {
        val divideEastOf = nextInt(width - 1)
        val passageAt = nextInt(height)
        repeat(height) { y ->
            if (passageAt != y) {
                val cell = grid[row + y, column + divideEastOf]!!
                cell.unlink(cell.east as CartesianCell)
            }
        }
        divide(grid, row, column, height, divideEastOf + 1)
        divide(grid, row, column + divideEastOf + 1, height, width - divideEastOf - 1)
    }
}