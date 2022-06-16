package io.github.ocirne.mazes

import java.awt.Color
import java.awt.image.BufferedImage
import java.awt.image.RenderedImage
import kotlin.random.Random

class Grid(val rows: Int, val columns: Int) {

    val grid: Array<Array<Cell>>

    init {
        grid = prepareGrid()
        configureCells()
    }

    fun prepareGrid(): Array<Array<Cell>> {
        return Array(rows) { row -> Array(columns) { column -> Cell(row, column) } }
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

    operator fun get(row: Int, column: Int): Cell? {
        if (row < 0 || rows <= row) {
            return null
        }
        if (column < 0 || columns <= column) {
            return null
        }
        return grid[row][column]
    }

    fun randomCell(): Cell? {
        val row = Random.nextInt(rows)
        val column = Random.nextInt(grid[row].size)

        return this[row, column]
    }

    fun size(): Int {
        return rows * columns
    }

    fun eachRow(): Iterator<Array<Cell>> {
        return grid.iterator()
    }

    fun eachCell(): List<Cell> {
        return grid.flatten()
    }

    fun toImage(cell_size: Int = 10): RenderedImage {
        val imgWidth = cell_size * columns
        val imgHeight = cell_size * rows

        val background = Color.BLACK
        val wall = Color.WHITE

        val image = BufferedImage(imgWidth + 1, imgHeight + 1, BufferedImage.TYPE_INT_RGB)
        val g = image.createGraphics()
        g.background = background
        g.color = wall

        for (cell in eachCell()) {
            val x1 = cell.column * cell_size
            val y1 = cell.row * cell_size
            val x2 = (cell.column + 1) * cell_size
            val y2 = (cell.row + 1) * cell_size

            if (cell.north == null)
                g.drawLine(x1, y1, x2, y1)
            if (cell.west == null)
                g.drawLine(x1, y1, x1, y2)

            if (!cell.isLinked(cell.east))
                g.drawLine(x2, y1, x2, y2)
            if (!cell.isLinked(cell.south))
                g.drawLine(x1, y2, x2, y2)
        }

        return image
    }
}