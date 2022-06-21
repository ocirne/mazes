package io.github.ocirne.mazes.grids

import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.grids.Grid.MODES.BACKGROUNDS
import io.github.ocirne.mazes.grids.Grid.MODES.WALLS
import java.awt.Color
import java.awt.image.BufferedImage
import java.awt.image.RenderedImage


class CartesianGrid(private val rows: Int, private val columns: Int) : Grid {

    private val grid: Array<Array<CartesianCell>>

    init {
        grid = prepareGrid()
        configureCells()
    }

    private fun prepareGrid(): Array<Array<CartesianCell>> {
        return Array(rows) { row -> Array(columns) { column -> CartesianCell(row, column) } }
    }

    private fun configureCells() {
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

    override fun size(): Int {
        return rows * columns
    }

    fun eachRow(): Iterator<Array<CartesianCell>> {
        return grid.iterator()
    }

    override fun eachCell(): List<CartesianCell> {
        return grid.flatten()
    }

    override fun toImage(
        cellSize: Int,
        wallInset: Double,
        backInset: Double,
        colorization: Colorization
    ): RenderedImage {
        val imgWidth = cellSize * columns
        val imgHeight = cellSize * rows
        val wallInsetAbsolute = (cellSize * wallInset).toInt()
        val backInsetAbsolute = (cellSize * backInset).toInt()

        val background = Color.BLACK

        val image = BufferedImage(imgWidth + 1, imgHeight + 1, BufferedImage.TYPE_INT_RGB)
        val g = image.createGraphics()
        g.background = background

        for (mode in Grid.MODES.values()) {
            for (cell in eachCell()) {
                if (cell.links().isEmpty())
                    continue
                cell.prepareCoordinates(cellSize, wallInsetAbsolute, backInsetAbsolute)
                when (mode) {
                    BACKGROUNDS -> {
                        cell.drawBackground(g, colorization)
                    }
                    WALLS -> {
                        cell.drawWalls(g, colorization)
                    }
                }
            }
        }
        return image
    }
}
