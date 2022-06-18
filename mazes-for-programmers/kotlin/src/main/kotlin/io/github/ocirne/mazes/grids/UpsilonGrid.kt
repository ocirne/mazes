package io.github.ocirne.mazes.grids

import io.github.ocirne.mazes.colorization.Colorization
import java.awt.Color
import java.awt.Graphics
import java.awt.Point
import java.awt.Polygon
import java.awt.image.BufferedImage
import java.awt.image.RenderedImage
import kotlin.math.sqrt

class UpsilonGrid(private val rows: Int, private val columns: Int) : Grid<UpsilonCell> {

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

    private fun drawline(g: Graphics, p1: Point, p2: Point) {
        g.drawLine(p1.x, p1.y, p2.x, p2.y)
    }

    override fun toImage(cellSize: Int, colorization: Colorization): RenderedImage {
        val cSize = cellSize
        val halfCSize = cSize / 2.0
        val aSize = cellSize / sqrt(2.0)
        val correctedSize = cSize + aSize

        val imgWidth = (correctedSize * (columns + 1)).toInt()
        val imgHeight = (correctedSize * (rows + 1)).toInt()

        val background = Color.BLACK

        val image = BufferedImage(imgWidth + 1, imgHeight + 1, BufferedImage.TYPE_INT_RGB)
        val g = image.createGraphics()
        g.background = background

        for (mode in Grid.MODES.values()) {
            for (cell in eachCell()) {
                val cx = (cell.column + 1) * correctedSize
                val cy = (cell.row + 1) * correctedSize

                val x0 = (cx - halfCSize - aSize).toInt()
                val x1 = (cx - halfCSize).toInt()
                val x2 = (cx + halfCSize).toInt()
                val x3 = (cx + halfCSize + aSize).toInt()

                val y0 = (cy - halfCSize - aSize).toInt()
                val y1 = (cy - halfCSize).toInt()
                val y2 = (cy + halfCSize).toInt()
                val y3 = (cy + halfCSize + aSize).toInt()

                var p0: Point
                var p1: Point
                var p2: Point
                var p3: Point
                var p4: Point
                var p5: Point
                var p6: Point
                var p7: Point

                if (cell.isOctogon()) {
                    p0 = Point(x0, y2)
                    p1 = Point(x0, y1)
                    p2 = Point(x1, y0)
                    p3 = Point(x2, y0)
                    p4 = Point(x3, y1)
                    p5 = Point(x3, y2)
                    p6 = Point(x2, y3)
                    p7 = Point(x1, y3)
                } else {
                    p0 = Point(x1, y2)
                    p7 = p0
                    p2 = Point(x1, y1)
                    p1 = p2
                    p4 = Point(x2, y1)
                    p3 = p4
                    p6 = Point(x2, y2)
                    p5 = p6
                }

                if (mode == Grid.MODES.BACKGROUNDS) {
                    val color = colorization.colorForBackground(cell)
                    if (color != null) {
                        g.color = color
                        val p = Polygon()
                        if (cell.isOctogon()) {
                            p.addPoint(p0.x, p0.y)
                            p.addPoint(p1.x, p1.y)
                            p.addPoint(p2.x, p2.y)
                            p.addPoint(p3.x, p3.y)
                            p.addPoint(p4.x, p4.y)
                            p.addPoint(p5.x, p5.y)
                            p.addPoint(p6.x, p6.y)
                            p.addPoint(p7.x, p7.y)
                        } else {
                            p.addPoint(p0.x, p0.y)
                            p.addPoint(p2.x, p2.y)
                            p.addPoint(p4.x, p4.y)
                            p.addPoint(p6.x, p6.y)
                        }
                        g.fillPolygon(p)
                    }
                } else {
                    g.color = colorization.colorForWall(cell)
                    if (cell.west == null)
                        drawline(g, p0, p1)
                    if (cell.north == null)
                        drawline(g, p2, p3)
                    if (!cell.isLinked(cell.east))
                        drawline(g, p4, p5)
                    if (!cell.isLinked(cell.south))
                        drawline(g, p6, p7)

                    if (cell.isOctogon()) {
                        if (cell.northwest == null)
                            drawline(g, p1, p2)
                        if (!cell.isLinked(cell.northeast))
                            drawline(g, p3, p4)
                        if (!cell.isLinked(cell.southeast))
                            drawline(g, p5, p6)
                        if (cell.southwest == null)
                            drawline(g, p7, p0)
                    }
                }
            }
        }
        return image
    }
}
