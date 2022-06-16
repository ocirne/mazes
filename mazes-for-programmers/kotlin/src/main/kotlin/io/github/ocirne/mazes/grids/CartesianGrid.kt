package io.github.ocirne.mazes.grids

import io.github.ocirne.mazes.colorization.Colorization
import java.awt.Color
import java.awt.image.BufferedImage
import java.awt.image.RenderedImage


class CartesianGrid(private val rows: Int, private val columns: Int) : Grid<CartesianCell> {

    val grid: Array<Array<CartesianCell>>

    init {
        grid = prepareGrid()
        configureCells()
    }

    fun prepareGrid(): Array<Array<CartesianCell>> {
        return Array(rows) { row -> Array(columns) { column -> CartesianCell(row, column) } }
    }

    fun configureCells() {
        for (cell in eachCell()) {
            val row = cell.row
            val col = cell.column

            cell.north = this[row - 1, col]
            cell.south = this[row + 1, col]
            cell.west = this[row, col - 1]
            cell.east = this[row, col + 1]
        }
    }

    override operator fun get(row: Int, column: Int): CartesianCell? {
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

    fun eachRow(): Iterator<Array<CartesianCell>> {
        return grid.iterator()
    }

    override fun eachCell(): List<CartesianCell> {
        return grid.flatten()
    }

    override fun toImage(cellSize: Int, colorization: Colorization): RenderedImage {
        val imgWidth = cellSize * columns
        val imgHeight = cellSize * rows

        val background = Color.BLACK
        val wall = Color.WHITE

        val image = BufferedImage(imgWidth + 1, imgHeight + 1, BufferedImage.TYPE_INT_RGB)
        val g = image.createGraphics()
        g.background = background

        for (mode in Grid.MODES.values()) {
            for (cell in eachCell()) {
                val x1 = cell.column * cellSize
                val y1 = cell.row * cellSize
                val x2 = (cell.column + 1) * cellSize
                val y2 = (cell.row + 1) * cellSize

                if (mode == Grid.MODES.BACKGROUNDS) {
                    val color = colorization.colorForBackground(cell)
                    if (color != null) {
                        g.color = color
                        g.fillRect(x1, y1, x2-x1, y2-y1)
                    }
                } else {
                    g.color = colorization.colorForWall(cell)
                    if (cell.north == null)
                        g.drawLine(x1, y1, x2, y1)
                    if (cell.west == null)
                        g.drawLine(x1, y1, x1, y2)
                    if (!cell.isLinked(cell.east))
                        g.drawLine(x2, y1, x2, y2)
                    if (!cell.isLinked(cell.south))
                        g.drawLine(x1, y2, x2, y2)
                }
            }
        }
        return image
    }
}
