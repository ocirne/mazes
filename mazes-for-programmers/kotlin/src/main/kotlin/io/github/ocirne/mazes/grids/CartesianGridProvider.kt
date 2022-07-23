package io.github.ocirne.mazes.grids

class CartesianGridProvider(private val rows: Int, private val columns: Int) {

    fun createPassageCarver(): CartesianGrid {
        return CartesianGrid(rows, columns)
    }

    fun createWallAdder(): CartesianGrid {
        val grid = CartesianGrid(rows, columns)
        for (cell in grid.eachCell()) {
            cell.neighbors().forEach { n -> cell.link(n, false) }
        }
        return grid
    }
}
