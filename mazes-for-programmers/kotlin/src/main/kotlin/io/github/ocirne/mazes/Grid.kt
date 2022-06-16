package io.github.ocirne.mazes

import java.awt.Color
import java.awt.image.BufferedImage
import java.awt.image.RenderedImage
import kotlin.math.roundToInt
import kotlin.random.Random

open class Grid(open val rows: Int, val columns: Int) {

    val grid: Array<Array<Cell>>

    // TODO move to ColoredGrid
    var distances: Distances? = null

    fun backgroundColorFor(cell: Cell): Color? {
        if (distances == null) {
            return null
        }
        if (distances!![cell] == null) {
            return null
        }
        val distance = distances!![cell] ?: return null
        val maximum = distances!!.max()!!.value
        val intensity = (maximum - distance).toFloat() / maximum
        val dark = (255 * intensity).roundToInt()
        val bright = 128 + (127 * intensity).roundToInt()
        return Color(dark, bright, dark)
    }

    init {
        grid = prepareGrid()
        configureCells()
    }

    open fun prepareGrid(): Array<Array<Cell>> {
        return Array(rows) { row -> Array(columns) { column -> Cell(row, column) } }
    }

    open fun configureCells() {
        for (cell in eachCell()) {
            val row = cell.row
            val col = cell.column

            cell.north = this[row - 1, col]
            cell.south = this[row + 1, col]
            cell.west = this[row, col - 1]
            cell.east = this[row, col + 1]
        }
    }

    open operator fun get(row: Int, column: Int): Cell? {
        if (row < 0 || rows <= row) {
            return null
        }
        if (column < 0 || columns <= column) {
            return null
        }
        return grid[row][column]
    }

    open fun randomCell(): Cell {
        while (true) {
            val row = Random.nextInt(rows)
            val column = Random.nextInt(grid[row].size)
            val cell = this[row, column]
            if (cell != null)
                return cell
        }
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

    enum class modes {
        BACKGROUNDS,
        WALLS
    }
    open fun toImage(cellSize: Int = 10): RenderedImage {
        val imgWidth = cellSize * columns
        val imgHeight = cellSize * rows

        val background = Color.BLACK
        val wall = Color.WHITE

        val image = BufferedImage(imgWidth + 1, imgHeight + 1, BufferedImage.TYPE_INT_RGB)
        val g = image.createGraphics()
        g.background = background

        for (mode in modes.values()) {
            for (cell in eachCell()) {
                val x1 = cell.column * cellSize
                val y1 = cell.row * cellSize
                val x2 = (cell.column + 1) * cellSize
                val y2 = (cell.row + 1) * cellSize

                if (mode == modes.BACKGROUNDS) {
                    val color = backgroundColorFor(cell)
                    if (color != null) {
                        g.color = color
                        g.fillRect(x1, y1, x2-x1, y2-y1)
                    }
                } else {
                    g.color = wall
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
