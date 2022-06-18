package io.github.ocirne.mazes.grids

import io.github.ocirne.mazes.colorization.Colorization
import java.awt.Color
import java.awt.Graphics
import java.awt.Point
import java.awt.Polygon
import java.awt.image.BufferedImage
import java.awt.image.RenderedImage
import kotlin.math.sqrt

class HexGrid(private val rows: Int, private val columns: Int) : Grid<HexCell> {

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

    private fun drawline(g: Graphics, p1: Point, p2: Point) {
        g.drawLine(p1.x, p1.y, p2.x, p2.y)
    }

    override fun toImage(cellSize: Int, colorization: Colorization): RenderedImage {
        val a_size = cellSize / 2.0
        val b_size = cellSize * sqrt(3.0) / 2.0
        val width = cellSize * 2.0
        val height = b_size * 2.0

        val imgWidth = (3 * a_size * columns + a_size + 0.5).toInt()
        val imgHeight = (height * rows + b_size + 0.5).toInt()

        val background = Color.BLACK

        val image = BufferedImage(imgWidth + 1, imgHeight + 1, BufferedImage.TYPE_INT_RGB)
        val g = image.createGraphics()
        g.background = background

        for (mode in Grid.MODES.values()) {
            for (cell in eachCell()) {
                val cx = cellSize + 3 * cell.column * a_size
                val cy = b_size + cell.row * height + (cell.column % 2) * b_size

                val x_fw = (cx - cellSize).toInt()
                val x_nw = (cx - a_size).toInt()
                val x_ne = (cx + a_size).toInt()
                val x_fe = (cx + cellSize).toInt()

                val y_n = (cy - b_size).toInt()
                val y_m = cy.toInt()
                val y_s = (cy + b_size).toInt()

                if (mode == Grid.MODES.BACKGROUNDS) {
                    val color = colorization.colorForBackground(cell)
                    if (color != null) {
                        g.color = color
                        val p = Polygon()
                        p.addPoint(x_fw, y_m)
                        p.addPoint(x_nw, y_n)
                        p.addPoint(x_ne, y_n)
                        p.addPoint(x_fe, y_m)
                        p.addPoint(x_ne, y_s)
                        p.addPoint(x_nw, y_s)
                        g.fillPolygon(p)
                    }
                } else {
                    g.color = colorization.colorForWall(cell)
                    if (cell.southwest == null)
                        g.drawLine(x_fw, y_m, x_nw, y_s)
                    if (cell.northwest == null)
                        g.drawLine(x_fw, y_m, x_nw, y_n)
                    if (cell.north == null)
                        g.drawLine(x_nw, y_n, x_ne, y_n)
                    if (!cell.isLinked(cell.northeast))
                        g.drawLine(x_ne, y_n, x_fe, y_m)
                    if (!cell.isLinked(cell.southeast))
                        g.drawLine(x_fe, y_m, x_ne, y_s)
                    if (!cell.isLinked(cell.south))
                        g.drawLine(x_ne, y_s, x_nw, y_s)
                }
            }
        }
        return image
    }
}
