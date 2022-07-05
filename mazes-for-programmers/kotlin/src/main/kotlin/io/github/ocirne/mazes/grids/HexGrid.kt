package io.github.ocirne.mazes.grids

import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.colorization.Strokes
import io.github.ocirne.mazes.output.createImage
import java.awt.image.RenderedImage
import kotlin.math.sqrt

class HexGrid(private val rows: Int, private val columns: Int) : Grid {

    /**
     * area = 6 * area_triangle (see TriangleGrid)
     * f = sqrt(1 / area)
     */
    private val correctionFactor = 0.6204032394013997

    private val grid: Array<Array<HexCell>>

    init {
        grid = prepareGrid()
        configureCells()
    }

    private fun prepareGrid(): Array<Array<HexCell>> {
        return Array(rows) { row -> Array(columns) { column -> HexCell(row, column) } }
    }

    private fun configureCells() {
        for (cell in eachCell()) {
            val row = cell.row
            val col = cell.column
            val northDiagonal = row - 1 + (col % 2)
            val southDiagonal = row + (col % 2)
            cell.northwest = this[northDiagonal, col - 1]
            cell.north     = this[row - 1, col]
            cell.northeast = this[northDiagonal, col + 1]
            cell.southwest = this[southDiagonal, col - 1]
            cell.south     = this[row + 1, col]
            cell.southeast = this[southDiagonal, col + 1]
        }
    }

    override operator fun get(row: Int, column: Int): HexCell? {
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

    override fun eachCell(): List<HexCell> {
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
        val aSize = cellSize / 2.0
        val bSize = cellSize * sqrt(3.0) / 2.0
        val height = bSize * 2.0

        val imgWidth = 3 * aSize * columns + aSize + 0.5
        val imgHeight = height * rows + bSize + 0.5

        val (image, g) = createImage(imgWidth + 1, imgHeight + 1)

        for (mode in Grid.MODES.values()) {
            for (cell in eachCell()) {
                cell.prepareCoordinates(cellSize, height, aSize, bSize)
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
