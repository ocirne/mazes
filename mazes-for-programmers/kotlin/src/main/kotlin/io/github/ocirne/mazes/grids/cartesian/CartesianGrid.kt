package io.github.ocirne.mazes.grids.cartesian

import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.colorization.Strokes
import io.github.ocirne.mazes.grids.Cell
import io.github.ocirne.mazes.grids.GridProvider
import io.github.ocirne.mazes.grids.Maze
import io.github.ocirne.mazes.output.createImage
import java.awt.image.BufferedImage

open class CartesianGrid(private val rows: Int, private val columns: Int): GridProvider {

    override fun forPassageCarver(): CartesianMaze {
        return CartesianMaze(rows, columns)
    }

    override fun forWallAdder(): CartesianMaze {
        val grid = CartesianMaze(rows, columns)
        for (cell in grid.eachCell()) {
            cell.neighbors().forEach { n -> cell.link(n, false) }
        }
        return grid
    }

    open class CartesianMaze(private val rows: Int, private val columns: Int) : Maze {

        private val correctionFactor = 1.0

        var grid: Array<Array<CartesianCell>>

        init {
            grid = prepareGrid()
            configureCells()
        }

        private fun prepareGrid(): Array<Array<CartesianCell>> {
            return Array(rows) { row -> Array(columns) { column -> CartesianCell(row, column) } }
        }

        fun configureCells() {
            for (cell in eachCell()) {
                val row = cell.row
                val col = cell.column

                cell.north = this[row - 1, col]
                cell.south = this[row + 1, col]
                cell.west = this[row, col - 1]
                cell.east = this[row, col + 1]
            }
        }

        override operator fun get(row: Int, column: Int): CartesianCell? {
            if (row < 0 || rows <= row) {
                return null
            }
            if (column < 0 || columns <= column) {
                return null
            }
            return grid[row][column]
        }

        fun getRows(): Int {
            return rows
        }

        fun getColumns(): Int {
            return columns
        }

        override fun size(): Int {
            return rows * columns
        }

        override fun eachRow(reversed: Boolean): Iterator<Array<CartesianCell>> {
            if (reversed) {
                throw NotImplementedError()
            }
            return grid.iterator()
        }

        override fun eachCell(): List<CartesianCell> {
            return grid.flatten()
        }

        override fun toImage(
            baseSize: Double,
            wallInset: Double,
            backInset: Double,
            drawDeadCells: Boolean,
            debug: Boolean,
            backgroundColors: Colorization,
            wallColors: Colorization,
            path: Colorization,
            marker: Colorization,
            strokes: Strokes
        ): BufferedImage {
            val cellSize = correctionFactor * baseSize
            val imgWidth = cellSize * columns
            val imgHeight = cellSize * rows
            val (image, g) = createImage(imgWidth + 1, imgHeight + 1)
            return image
        }
    }

    open class CartesianCell(val row: Int, val column: Int) : Cell() {

        var north: CartesianCell? = null
        var south: CartesianCell? = null
        var east: CartesianCell? = null
        var west: CartesianCell? = null

        override fun neighbors(): List<CartesianCell> {
            return arrayListOf(north, south, east, west).filterNotNull()
        }


        data class Coordinates(
            val withInset: Boolean,
            val x1: Double,
            val x2: Double,
            val x3: Double,
            val x4: Double,
            val y1: Double,
            val y2: Double,
            val y3: Double,
            val y4: Double
        )

        lateinit var c: Coordinates

    }
}