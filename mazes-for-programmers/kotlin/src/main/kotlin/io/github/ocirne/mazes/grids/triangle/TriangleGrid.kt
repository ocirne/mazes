package io.github.ocirne.mazes.grids.triangle

import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.colorization.Strokes
import io.github.ocirne.mazes.grids.Cell
import io.github.ocirne.mazes.grids.GridProvider
import io.github.ocirne.mazes.grids.Maze
import io.github.ocirne.mazes.output.createImage
import java.awt.Graphics2D
import java.awt.Polygon
import java.awt.image.BufferedImage
import kotlin.math.sqrt

class TriangleGrid(private val rows: Int, private val columns: Int) : GridProvider {

    override fun forPassageCarver(): TriangleMaze {
        return TriangleMaze(rows, columns)
    }

    override fun forWallAdder(): TriangleMaze {
        val grid = TriangleMaze(rows, columns)
        for (cell in grid.eachCell()) {
            cell.neighbors().forEach { n -> cell.link(n, false) }
        }
        return grid
    }

    class TriangleMaze(private val rows: Int, private val columns: Int) : Maze {

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
        ): BufferedImage {
            val cellSize = correctionFactor * baseSize
            val halfWidth = cellSize / 2.0
            val height = cellSize * sqrt(3.0) / 2.0
            val halfHeight = height / 2.0

            val imgWidth = cellSize * (columns + 1) / 2.0
            val imgHeight = height * rows

            val (image, g) = createImage(imgWidth + 1, imgHeight + 1)

            for (mode in Maze.MODES.values()) {
                for (cell in eachCell()) {
                    cell.prepareCoordinates(halfWidth, height, halfHeight)
                    if (mode == Maze.MODES.BACKGROUNDS) {
                        cell.drawBackground(g, backgroundColors)
                    } else {
                        cell.drawWalls(g, wallColors, strokes)
                    }
                }
            }
            return image
        }
    }

    class TriangleCell(row: Int, column: Int) : Cell(row, column) {

        var east: TriangleCell? = null
        var west: TriangleCell? = null
        var north: TriangleCell? = null
        var south: TriangleCell? = null

        override fun neighbors(): List<TriangleCell> {
            return arrayListOf(west, east, if (isUpright()) south else north).filterNotNull()
        }

        fun isUpright(): Boolean {
            return (row + column) % 2 == 0
        }

        data class Coordinates(
            val westX: Int,
            val midX: Int,
            val eastX: Int,
            val apexY: Int,
            val baseY: Int
        )

        private lateinit var c: Coordinates

        fun prepareCoordinates(halfWidth: Double, height: Double, halfHeight: Double) {
            val cx = halfWidth + column * halfWidth
            val cy = halfHeight + row * height

            val westX = (cx - halfWidth).toInt()
            val midX = cx.toInt()
            val eastX = (cx + halfWidth).toInt()

            val apexY: Int
            val baseY: Int

            if (isUpright()) {
                apexY = (cy - halfHeight).toInt()
                baseY = (cy + halfHeight).toInt()
            } else {
                apexY = (cy + halfHeight).toInt()
                baseY = (cy - halfHeight).toInt()
            }
            c = Coordinates(westX, midX, eastX, apexY, baseY)
        }

        override fun drawBackground(g: Graphics2D, colorization: Colorization) {
            g.color = colorization.valueFor(this)
            val p = Polygon()
            p.addPoint(c.westX, c.baseY)
            p.addPoint(c.midX, c.apexY)
            p.addPoint(c.eastX, c.baseY)
            g.fillPolygon(p)
        }

        override fun drawWalls(g: Graphics2D, colorization: Colorization, strokes: Strokes) {
            g.color = colorization.valueFor(this)
            g.stroke = strokes.getBasicWall()
            if (west == null)
                g.drawLine(c.westX, c.baseY, c.midX, c.apexY)
            if (!isLinked(east))
                g.drawLine(c.eastX, c.baseY, c.midX, c.apexY)
            val noSouth = isUpright() && south == null
            val notLinked = !isUpright() && !isLinked(north)
            if (noSouth || notLinked)
                g.drawLine(c.eastX, c.baseY, c.westX, c.baseY)

        }
    }

}