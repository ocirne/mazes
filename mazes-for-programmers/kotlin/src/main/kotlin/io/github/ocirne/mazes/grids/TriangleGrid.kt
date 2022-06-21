package io.github.ocirne.mazes.grids

import io.github.ocirne.mazes.colorization.Colorization
import java.awt.Color
import java.awt.Graphics
import java.awt.Point
import java.awt.image.BufferedImage
import java.awt.image.RenderedImage
import kotlin.math.sqrt

class TriangleGrid(private val rows: Int, private val columns: Int) : Grid {

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

    override fun toImage(cellSize: Int, wallInset:Double, backInset: Double, colorization: Colorization): RenderedImage {
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
                cell.prepareCoordinates(halfWidth, height, halfHeight)
                if (mode == Grid.MODES.BACKGROUNDS) {
                    cell.drawBackground(g, colorization)
                } else {
                    cell.drawWalls(g, colorization)
                }
            }
        }
        return image
    }
}
