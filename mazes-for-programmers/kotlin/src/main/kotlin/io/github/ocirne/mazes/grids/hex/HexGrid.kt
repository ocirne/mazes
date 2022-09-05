package io.github.ocirne.mazes.grids.hex

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

class HexGrid(private val rows: Int, private val columns: Int): GridProvider {

    override fun forPassageCarver(): HexMaze {
        return HexMaze(rows, columns)
    }

    override fun forWallAdder(): HexMaze {
        val grid = HexMaze(rows, columns)
        for (cell in grid.eachCell()) {
            cell.neighbors().forEach { n -> cell.link(n, false) }
        }
        return grid
    }

    class HexMaze(private val rows: Int, private val columns: Int) : Maze {

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
        ): BufferedImage {
            val cellSize = correctionFactor * baseSize
            val aSize = cellSize / 2.0
            val bSize = cellSize * sqrt(3.0) / 2.0
            val height = bSize * 2.0

            val imgWidth = 3 * aSize * columns + aSize + 0.5
            val imgHeight = height * rows + bSize + 0.5

            val (image, g) = createImage(imgWidth + 1, imgHeight + 1)

            for (mode in Maze.MODES.values()) {
                for (cell in eachCell()) {
                    cell.prepareCoordinates(cellSize, height, aSize, bSize)
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

    class HexCell(val row: Int, val column: Int) : Cell() {

        var north: HexCell? = null
        var northeast: HexCell? = null
        var southeast: HexCell? = null
        var south: HexCell? = null
        var southwest: HexCell? = null
        var northwest: HexCell? = null

        override fun neighbors(): List<HexCell> {
            return arrayListOf(north, northeast, southeast, south, southwest, northwest).filterNotNull()
        }

        data class Coordinates(
            val xFW: Int,
            val xNW: Int,
            val xNE: Int,
            val xFE: Int,

            val yN: Int,
            val yM: Int,
            val yS: Int
        )

        private lateinit var c: Coordinates

        fun prepareCoordinates(cellSize: Double, height: Double, aSize:Double, bSize:Double) {
            val cx = cellSize + 3 * column * aSize
            val cy = bSize + row * height + (column % 2) * bSize

            val xFW = (cx - cellSize).toInt()
            val xNW = (cx - aSize).toInt()
            val xNE = (cx + aSize).toInt()
            val xFE = (cx + cellSize).toInt()

            val yN = (cy - bSize).toInt()
            val yM = cy.toInt()
            val yS = (cy + bSize).toInt()
            c = Coordinates(xFW, xNW, xNE, xFE, yN, yM, yS)
        }

        override fun drawBackground(g: Graphics2D, colorization: Colorization) {
            g.color = colorization.valueFor(this)
            // TODO switch to Path2d (all polygons)
            val p = Polygon()
            p.addPoint(c.xFW, c.yM)
            p.addPoint(c.xNW, c.yN)
            p.addPoint(c.xNE, c.yN)
            p.addPoint(c.xFE, c.yM)
            p.addPoint(c.xNE, c.yS)
            p.addPoint(c.xNW, c.yS)
            g.fillPolygon(p)
        }

        override fun drawWalls(g: Graphics2D, colorization: Colorization, strokes: Strokes) {
            g.color = colorization.valueFor(this)
            g.stroke = strokes.getBasicWall()
            if (southwest == null)
                g.drawLine(c.xFW, c.yM, c.xNW, c.yS)
            if (northwest == null)
                g.drawLine(c.xFW, c.yM, c.xNW, c.yN)
            if (north == null)
                g.drawLine(c.xNW, c.yN, c.xNE, c.yN)
            if (!isLinked(northeast))
                g.drawLine(c.xNE, c.yN, c.xFE, c.yM)
            if (!isLinked(southeast))
                g.drawLine(c.xFE, c.yM, c.xNE, c.yS)
            if (!isLinked(south))
                g.drawLine(c.xNE, c.yS, c.xNW, c.yS)
        }
    }
}