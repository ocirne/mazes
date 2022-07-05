package io.github.ocirne.mazes.grids

import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.colorization.Strokes
import io.github.ocirne.mazes.output.createImage
import java.awt.image.RenderedImage
import kotlin.math.sqrt

class TriangleGrid(private val rows: Int, private val columns: Int) : Grid {

    /**
     * g = 1
     * h = g * sqrt(3) / 2
     * area = g * h / 2 = sqrt(3) / 4
     * f = sqrt(1 / area) = sqrt(4 / sqrt(3))
     */
    private val correctionFactor = 1.5196713713031853

    private val grid: Array<Array<TriangleCell>>

    init {
        grid = prepareGrid()
        configureCells()
    }

    private fun prepareGrid(): Array<Array<TriangleCell>> {
        return Array(rows) { row -> Array(columns) { column -> TriangleCell(row, column) } }
    }

    private fun configureCells() {
        for (cell in eachCell()) {
            val row = cell.row
            val col = cell.column
            cell.west = this[row, col - 1]
            cell.east = this[row, col + 1]
            if (cell.isUpright())
                cell.south = this[row + 1, col]
            else
                cell.north = this[row - 1, col]
        }
    }

    override operator fun get(row: Int, column: Int): TriangleCell? {
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

    override fun eachCell(): List<TriangleCell> {
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
        val halfWidth = cellSize / 2.0
        val height = cellSize * sqrt(3.0) / 2.0
        val halfHeight = height / 2.0

        val imgWidth = cellSize * (columns + 1) / 2.0
        val imgHeight = height * rows

        val (image, g) = createImage(imgWidth + 1, imgHeight + 1)

        for (mode in Grid.MODES.values()) {
            for (cell in eachCell()) {
                cell.prepareCoordinates(halfWidth, height, halfHeight)
                if (mode == Grid.MODES.BACKGROUNDS) {
                    cell.drawBackground(g, backgroundColors)
                } else {
                    cell.drawWalls(g, wallColors, strokes)
                }
            }
        }
        return image
    }
}
