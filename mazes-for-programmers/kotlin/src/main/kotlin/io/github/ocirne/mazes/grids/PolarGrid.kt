package io.github.ocirne.mazes.grids

import io.github.ocirne.mazes.colorization.Colorization
import java.awt.Color
import java.awt.image.BufferedImage
import java.awt.image.RenderedImage
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

class PolarGrid(private val rows: Int) : Grid<PolarCell> {

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

    override operator fun get(row: Int, column: Int): PolarCell? {
        if (row < 0 || rows <= row) {
            return null
        }
        return grid[row][Math.floorMod(column, grid[row].size)]
    }

    override fun size(): Int {
        TODO("Not yet implemented")
    }

    override fun eachCell(): List<PolarCell> {
        return grid.flatten()
    }

    override fun toImage(cellSize: Int, colorization: Colorization): RenderedImage {
        val imgSize = 2 * rows * cellSize

        val image = BufferedImage(imgSize + 1, imgSize + 1, BufferedImage.TYPE_INT_RGB)
        val g = image.createGraphics()
        g.background = Color.BLACK

        val center = imgSize / 2

        for (cell in eachCell()) {
            val theta = 2 * PI / grid[cell.row].size
            val innerRadius = cell.row * cellSize
            val outerRadius = (cell.row + 1) * cellSize
            val thetaCcw = cell.column * theta
            val thetaCw = (cell.column + 1) * theta

            val ax = center + (innerRadius * cos(thetaCcw)).toInt()
            val ay = center + (innerRadius * sin(thetaCcw)).toInt()
            val cx = center + (innerRadius * cos(thetaCw)).toInt()
            val cy = center + (innerRadius * sin(thetaCw)).toInt()
            val dx = center + (outerRadius * cos(thetaCw)).toInt()
            val dy = center + (outerRadius * sin(thetaCw)).toInt()

            g.color = colorization.colorForWall(cell)
            if (!cell.isLinked(cell.inward))
                g.drawLine(ax, ay, cx, cy)
            if (!cell.isLinked(cell.cw))
                g.drawLine(cx, cy, dx, dy)
        }

        val radius = rows * cellSize
        g.drawOval(center - radius, center - radius, center + radius, center + radius)
        return image
    }
}
