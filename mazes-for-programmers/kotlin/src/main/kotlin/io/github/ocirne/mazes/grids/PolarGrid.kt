package io.github.ocirne.mazes.grids

import io.github.ocirne.mazes.colorization.Colorization
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.geom.Ellipse2D
import java.awt.geom.Line2D
import java.awt.image.BufferedImage
import java.awt.image.RenderedImage
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

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

    fun helperGrid(g: Graphics2D, cellSize: Int, center: Double, wallInset: Double) {
        val dashed = BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0.0f, floatArrayOf(9.0f), 0.0f)
        g.stroke = dashed;
        g.color = Color.WHITE
        val theta = 2 * PI / grid[rows-1]!!.size
        val outerRadius = rows * cellSize
        val radiusInset = wallInset * cellSize
        val innerRadius = cellSize - radiusInset
        val thetaInset = radiusInset * 2*PI / 6
        val thetaInsetInnen = thetaInset / innerRadius
        val thetaInsetAussen = thetaInset / outerRadius

        for (row in 0 .. rows) {
            val radius1 = row * cellSize - radiusInset
            g.draw(Ellipse2D.Double(center - radius1, center - radius1, 2.0 * radius1, 2.0 * radius1))
            val radius2 = row * cellSize + radiusInset
            g.draw(Ellipse2D.Double(center - radius2, center - radius2, 2.0 * radius2, 2.0 * radius2))
        }
        for (t in 0 .. grid[rows-1]!!.size) {
            val x1 = center + innerRadius * cos(t * theta + thetaInsetInnen)
            val y1 = center - innerRadius * sin(t * theta + thetaInsetInnen)
            val x2 = center + innerRadius * cos(t * theta - thetaInsetInnen)
            val y2 = center - innerRadius * sin(t * theta - thetaInsetInnen)

            val x3 = center + outerRadius * cos(t * theta + thetaInsetAussen)
            val y3 = center - outerRadius * sin(t * theta + thetaInsetAussen)
            val x4 = center + outerRadius * cos(t * theta - thetaInsetAussen)
            val y4 = center - outerRadius * sin(t * theta - thetaInsetAussen)

            g.draw(Line2D.Double(x1, y1, x3, y3))
            g.draw(Line2D.Double(x2, y2, x4, y4))
        }
    }

    override fun toImage(cellSize: Int, wallInset:Double, backInset: Double,
                         backgroundColors: Colorization,
                         wallColors: Colorization,
                         path: Colorization,
                         marker: Colorization): RenderedImage {
        val imgSize = 2 * rows * cellSize + 4

        val image = BufferedImage(imgSize + 1, imgSize + 1, BufferedImage.TYPE_INT_RGB)
        val g = image.createGraphics()
        // Smooth errors - einfach alles breitschmieren statt floodfill implementieren ;)
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)

        g.background = Color.BLACK

        val center = imgSize / 2

        helperGrid(g, cellSize, center.toDouble(), wallInset)

        for (mode in Grid.MODES.values()) {
            for (cell in eachCell()) {
                when (mode) {
                    Grid.MODES.BACKGROUNDS -> {
//                        cell.prepareCoordinates(this, center, cellSize, backInset)
//                        cell.drawBackground(g, backgroundColors)
                    }
                    Grid.MODES.WALLS -> {
                        if (cell.row == 0) { // TODO prüfen
                            continue
                        }
                        // TODO vielleicht trennen in coordinaten und insets?
                        cell.prepareCoordinates(this, center, cellSize, wallInset)
                        cell.drawWalls(g, wallColors)
                    }
                    Grid.MODES.PATH -> {
//                        cell.prepareCoordinates(this, center, cellSize)
//                        cell.drawPath(g, path, this, center, cellSize)
                    }
                    Grid.MODES.MARKER -> {
//                        cell.prepareCoordinates(this, center, cellSize)
//                        cell.drawMarker(g, marker, this, center, cellSize)
                    }
                }
            }
        }

        val radius = rows * cellSize
        g.color = Color.WHITE
        g.drawOval(center - radius, center - radius, 2*radius, 2*radius)
        return image
    }
}
