package io.github.ocirne.mazes

import java.awt.Color
import java.awt.image.BufferedImage
import java.awt.image.RenderedImage
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

class PolarGrid(override val rows: Int) : Grid(rows, 1) {

    override fun prepareGrid(): Array<Array<Cell>> {
        val rings = mutableListOf<Array<Cell>>()

        val rowHeight = 1.0 / super.rows.toFloat()
        rings.add(arrayOf(PolarCell(0, 0)))

        for (row in 1..super.rows) {
            val radius = row.toFloat() / super.rows
            val circumference = 2 * PI * radius

            val previousCount = rings[row - 1].size
            val estimatedCellWidth = circumference.toFloat() / previousCount
            val ratio = (estimatedCellWidth / rowHeight).roundToInt()

            val cells = previousCount * ratio
            rings.add(Array(cells) { col -> PolarCell(row, col) })
        }
        println(rings.size)
        println(Array(super.rows) { row -> rings[row] }.size)
        return Array(super.rows) { row -> rings[row] }
    }

    override fun configureCells() {
        for (cell in eachCell()) {
            val row = cell.row
            val col = cell.column

            cell as PolarCell

            if (row > 0) {
                cell.cw = this[row, col + 1]
                cell.ccw = this[row, col - 1]

                val ratio = grid[row].size / grid[row - 1].size
                val parent = grid[row - 1][col / ratio] as PolarCell
                parent.outward.add(cell)
                cell.inward = parent
            }
        }
    }

    override operator fun get(row: Int, column: Int): Cell? {
        if (row < 0 || super.rows <= row) {
            return null
        }
        return grid[row][Math.floorMod(column, grid[row].size)]
    }

    override fun toImage(cellSize: Int): RenderedImage {
        val imgSize = 2 * super.rows * cellSize

        val background = Color.BLACK
        val wall = Color.WHITE

        val image = BufferedImage(imgSize + 1, imgSize + 1, BufferedImage.TYPE_INT_RGB)
        val g = image.createGraphics()
        g.background = background
        g.color = wall

        val center = imgSize / 2

        for (cell in eachCell()) {
            val theta = 2 * PI / grid[cell.row].size
            val inner_radius = cell.row * cellSize
            val outer_radius = (cell.row + 1) * cellSize
            val theta_ccw = cell.column * theta
            val theta_cw = (cell.column + 1) * theta

            val ax = center + (inner_radius * cos(theta_ccw)).toInt()
            val ay = center + (inner_radius * sin(theta_ccw)).toInt()
            val cx = center + (inner_radius * cos(theta_cw)).toInt()
            val cy = center + (inner_radius * sin(theta_cw)).toInt()
            val dx = center + (outer_radius * cos(theta_cw)).toInt()
            val dy = center + (outer_radius * sin(theta_cw)).toInt()

            cell as PolarCell

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
