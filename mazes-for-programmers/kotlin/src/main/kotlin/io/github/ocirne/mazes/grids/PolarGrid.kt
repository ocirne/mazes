package io.github.ocirne.mazes.grids

import io.github.ocirne.mazes.colorization.Colorization
import java.awt.BasicStroke
import java.awt.Color
import java.awt.image.BufferedImage
import java.awt.image.RenderedImage
import kotlin.math.PI
import kotlin.math.roundToInt

class PolarGrid(private val rows: Int) : Grid {

    private val grid: Array<Array<PolarCell>>

    init {
        grid = prepareGrid()
        configureCells()
    }

    private fun prepareGrid(): Array<Array<PolarCell>> {
        val rings = mutableListOf<Array<PolarCell>>()

        val rowHeight = 1.0 / rows.toFloat()
        rings.add(arrayOf(PolarCell(0, 0)))

        for (row in 1..rows) {
            val radius = row.toFloat() / rows
            val circumference = 2 * PI * radius

            val previousCount = rings[row - 1].size
            val estimatedCellWidth = circumference.toFloat() / previousCount
            val ratio = (estimatedCellWidth / rowHeight).roundToInt()

            val cells = previousCount * ratio
            rings.add(Array(cells) { col -> PolarCell(row, col) })
        }
        return Array(rows) { row -> rings[row] }
    }

    private fun configureCells() {
        for (cell in eachCell()) {
            val row = cell.row
            val col = cell.column
            if (row > 0) {
                cell.cw = this[row, col + 1]
                cell.ccw = this[row, col - 1]

                val ratio = grid[row].size / grid[row - 1].size
                val parent = grid[row - 1][col / ratio]
                parent.outward.add(cell)
                cell.inward = parent
            }
        }
    }

    operator fun get(row: Int): Array<PolarCell>? {
        if (row < 0 || rows <= row) {
            return null
        }
        return grid[row]
    }

    override operator fun get(row: Int, column: Int): PolarCell? {
        if (row < 0 || rows <= row) {
            return null
        }
        return grid[row][Math.floorMod(column, grid[row].size)]
    }

    override fun size(): Int {
        return grid.flatten().size
    }

    override fun eachCell(): List<PolarCell> {
        return grid.flatten()
    }

    override fun toImage(cellSize: Int, wallInset:Double, backInset: Double, colorization: Colorization): RenderedImage {
        val imgSize = 2 * rows * cellSize + 4

        val image = BufferedImage(imgSize + 1, imgSize + 1, BufferedImage.TYPE_INT_RGB)
        val g = image.createGraphics()
        // Smooth errors - einfach alles breitschmieren statt floodfill implementieren ;)
        g.stroke = BasicStroke(2.0f)
        g.background = Color.BLACK

        val center = imgSize / 2

        for (mode in Grid.MODES.values()) {
            for (cell in eachCell()) {
                cell.prepareCoordinates(this, center, cellSize)
                if (mode == Grid.MODES.BACKGROUNDS) {
                    g.color = colorization.colorForBackground(cell)
                    cell.drawBackground(g)
                } else {
                    g.color = colorization.colorForWall(cell)
                    cell.drawWalls(g)
                }
            }
        }

        val radius = rows * cellSize
        g.drawOval(center - radius, center - radius, 2*radius, 2*radius)
        return image
    }
}
