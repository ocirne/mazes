package io.github.ocirne.mazes.grids

import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.colorization.Strokes
import io.github.ocirne.mazes.output.createImage
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.Ellipse2D
import java.awt.geom.Line2D
import java.awt.image.RenderedImage
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

open class PolarGrid(private val rows: Int) : Grid {

    private val correctionFactor = 1.0

    var grid: Array<Array<PolarCell>>

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

    fun configureCells() {
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

    private fun debugGrid(g: Graphics2D, cellSize: Double, center: Double, wallInset: Double) {
        val dashed = BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0.0f, floatArrayOf(9.0f), 0.0f)
        g.stroke = dashed
        g.color = Color.WHITE
        val theta = 2 * PI / grid[rows-1].size
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
        for (t in 0 .. grid[rows-1].size) {
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
        val imgSize = 2 * rows * cellSize + 4

        val (image, g) = createImage(imgSize + 1, imgSize + 1)
        val center = imgSize / 2

        if (debug)
            debugGrid(g, cellSize, center, wallInset)

        for (mode in Grid.MODES.values()) {
            for (cell in eachCell()) {
                when (mode) {
                    Grid.MODES.BACKGROUNDS -> {
                        cell.prepareCoordinates(this, center, cellSize, backInset)
                        cell.drawBackground(g, backgroundColors)
                    }
                    Grid.MODES.FAKE -> {
                        cell.prepareCoordinates(this, center, cellSize, backInset)
                        cell.drawSpaceBetweenWalls(g, 2 * backInset * cellSize)
                    }
                    Grid.MODES.WALLS -> {
                        // TODO vielleicht trennen in coordinaten und insets?
                        cell.prepareCoordinates(this, center, cellSize, wallInset)
                        cell.drawWalls(g, wallColors, strokes)
                    }
                    Grid.MODES.PATH -> {
                        cell.prepareCoordinates(this, center, cellSize)
                        cell.drawPath(g, path, strokes,this, center, cellSize)
                    }
                    Grid.MODES.MARKER -> {
                        cell.prepareCoordinates(this, center, cellSize)
                        cell.drawMarker(g, marker, this, center, cellSize)
                    }
                }
            }
        }

        if (wallInset == 0.0) {
            val radius = rows * cellSize
            g.color = wallColors.valueFor(grid[0][0])
            val xy = (center - radius).toInt()
            val wh = (2*radius).toInt()
            g.drawOval(xy, xy, wh, wh)
        }
        return image
    }
}
