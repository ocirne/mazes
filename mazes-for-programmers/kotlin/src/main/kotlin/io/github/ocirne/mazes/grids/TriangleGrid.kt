package io.github.ocirne.mazes.grids

import io.github.ocirne.mazes.colorization.Colorization
import java.awt.Color
import java.awt.Graphics
import java.awt.Point
import java.awt.Polygon
import java.awt.image.BufferedImage
import java.awt.image.RenderedImage
import kotlin.math.sqrt

class TriangleGrid(private val rows: Int, private val columns: Int) : Grid<TriangleCell> {

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

    private fun drawline(g: Graphics, p1: Point, p2: Point) {
        g.drawLine(p1.x, p1.y, p2.x, p2.y)
    }

    override fun toImage(cellSize: Int, colorization: Colorization): RenderedImage {
        val halfWidth = cellSize / 2.0
        val height = cellSize * sqrt(3.0) / 2.0
        val halfHeight = height / 2.0

        val imgWidth = (cellSize * (columns + 1) / 2.0).toInt()
        val imgHeight = (height * rows).toInt()

        val background = Color.BLACK

        val image = BufferedImage(imgWidth + 1, imgHeight + 1, BufferedImage.TYPE_INT_RGB)
        val g = image.createGraphics()
        g.background = background

        for (mode in Grid.MODES.values()) {
            for (cell in eachCell()) {
                val cx = halfWidth + cell.column * halfWidth
                val cy = halfHeight + cell.row * height

                val west_x = (cx - halfWidth).toInt()
                val mid_x = cx.toInt()
                val east_x = (cx + halfWidth).toInt()

                val apex_y: Int
                val base_y: Int

                if (cell.isUpright()) {
                    apex_y = (cy - halfHeight).toInt()
                    base_y = (cy + halfHeight).toInt()
                } else {
                    apex_y = (cy + halfHeight).toInt()
                    base_y = (cy - halfHeight).toInt()
                }

                if (mode == Grid.MODES.BACKGROUNDS) {
                    val color = colorization.colorForBackground(cell)
                    if (color != null) {
                        g.color = color
                        val p = Polygon()
                        p.addPoint(west_x, base_y)
                        p.addPoint(mid_x, apex_y)
                        p.addPoint(east_x, base_y)
                        g.fillPolygon(p)
                    }
                } else {
                    g.color = colorization.colorForWall(cell)
                    if (cell.west == null)
                        g.drawLine(west_x, base_y, mid_x, apex_y)
                    if (!cell.isLinked(cell.east))
                        g.drawLine(east_x, base_y, mid_x, apex_y)
                    val no_south = cell.isUpright() && cell.south == null
                    val not_linked = !cell.isUpright() && !cell.isLinked(cell.north)
                    if (no_south || not_linked)
                        g.drawLine(east_x, base_y, west_x, base_y)
                }
            }
        }
        return image
    }
}
