package io.github.ocirne.mazes.grids

import java.awt.Graphics

class CartesianCell(val row: Int, val column: Int) : Cell() {

    var north: CartesianCell? = null
    var south: CartesianCell? = null
    var east: CartesianCell? = null
    var west: CartesianCell? = null

    data class Coordinates(val x1: Int, val y1: Int, val x2: Int, val y2: Int)

    lateinit var c: Coordinates

    fun prepareCoordinates(cellSize: Int) {
        val x1 = column * cellSize
        val y1 = row * cellSize
        val x2 = (column + 1) * cellSize
        val y2 = (row + 1) * cellSize
        c = Coordinates(x1, y1, x2, y2)
    }

    override fun neighbors(): List<CartesianCell> {
        return arrayListOf(north, south, east, west).filterNotNull()
    }

    override fun drawBackground(g: Graphics) {
        g.fillRect(c.x1, c.y1, c.x2-c.x1, c.y2-c.y1)
    }

    override fun drawWalls(g: Graphics) {
        if (north == null)
            g.drawLine(c.x1, c.y1, c.x2, c.y1)
        if (west == null)
            g.drawLine(c.x1, c.y1, c.x1, c.y2)
        if (!isLinked(east))
            g.drawLine(c.x2, c.y1, c.x2, c.y2)
        if (!isLinked(south))
            g.drawLine(c.x1, c.y2, c.x2, c.y2)
    }
}
