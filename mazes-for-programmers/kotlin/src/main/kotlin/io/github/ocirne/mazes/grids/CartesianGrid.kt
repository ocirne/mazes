package io.github.ocirne.mazes.grids

import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.colorization.Strokes
import io.github.ocirne.mazes.grids.Grid.MODES.BACKGROUNDS
import io.github.ocirne.mazes.grids.Grid.MODES.WALLS
import io.github.ocirne.mazes.output.createImage
import java.awt.image.RenderedImage


open class CartesianGrid(private val rows: Int, private val columns: Int) : Grid {

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
    ): RenderedImage {
        val cellSize = correctionFactor * baseSize
        val imgWidth = cellSize * columns
        val imgHeight = cellSize * rows
        val wallInsetAbsolute = cellSize * wallInset
        val backInsetAbsolute = cellSize * backInset

        val (image, g) = createImage(imgWidth + 1, imgHeight + 1)

        for (mode in Grid.MODES.values()) {
            for (cell in eachCell()) {
                if (!drawDeadCells && cell.links().isEmpty())
                    continue
                when (mode) {
                    BACKGROUNDS -> {
                        cell.prepareCoordinates(cellSize, backInsetAbsolute)
                        cell.drawBackground(g, backgroundColors)
                    }
                    WALLS -> {
                        cell.prepareCoordinates(cellSize, wallInsetAbsolute)
                        cell.drawWalls(g, wallColors, strokes)
                    }
                    else -> { /* not implemented */ }
                }
            }
        }
        return image
    }
}
