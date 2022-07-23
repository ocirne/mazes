package io.github.ocirne.mazes.grids

import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.colorization.Strokes
import io.github.ocirne.mazes.output.createImage
import java.awt.image.BufferedImage
import kotlin.math.sqrt

class UpsilonGrid(private val rows: Int, private val columns: Int) : MutableGrid, GridProvider {

    /**
     * a = 1 / sqrt(2)
     * area_8 = (1*1) + 4 * (a * 1) + 4 * (a * a / 2)
     * area_4 = 1*1
     * area_avg = (area_8 + area_4) / 2
     * f = sqrt(1 / area_avg)
     */
    private val correctionFactor = 0.585786437626905

    private val grid: Array<Array<UpsilonCell>>

    init {
        grid = prepareGrid()
        configureCells()
    }

    private fun prepareGrid(): Array<Array<UpsilonCell>> {
        return Array(rows) { row -> Array(columns) { column -> UpsilonCell(row, column) } }
    }

    private fun configureCells() {
        for (cell in eachCell()) {
            val row = cell.row
            val col = cell.column
            cell.north = this[row - 1, col]
            cell.east = this[row, col + 1]
            cell.south = this[row + 1, col]
            cell.west = this[row, col - 1]
            if (cell.isOctogon()) {
                cell.northwest = this[row - 1, col - 1]
                cell.northeast = this[row - 1, col + 1]
                cell.southeast = this[row + 1, col + 1]
                cell.southwest = this[row + 1, col - 1]
            }
        }
    }

    override fun createPassageCarver(): UpsilonGrid {
        return UpsilonGrid(rows, columns)
    }

    override fun createWallAdder(): UpsilonGrid {
        val grid = UpsilonGrid(rows, columns)
        addAllPassages()
        return grid
    }

    override operator fun get(row: Int, column: Int): UpsilonCell? {
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

    override fun eachCell(): List<UpsilonCell> {
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
        val halfCSize = cellSize / 2.0
        val aSize = cellSize / sqrt(2.0)
        val correctedSize = cellSize + aSize

        val imgWidth = correctedSize * (columns + 1)
        val imgHeight = correctedSize * (rows + 1)

        val (image, g) = createImage(imgWidth + 1, imgHeight + 1)

        for (mode in MutableGrid.MODES.values()) {
            for (cell in eachCell()) {
                cell.prepareCoordinates(correctedSize, halfCSize, aSize)
                if (mode == MutableGrid.MODES.BACKGROUNDS) {
                    cell.drawBackground(g, backgroundColors)
                } else {
                    cell.drawWalls(g, wallColors, strokes)
                }
            }
        }
        return image
    }
}
