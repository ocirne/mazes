package io.github.ocirne.mazes.grids

import java.awt.Graphics

class CartesianCell(val row: Int, val column: Int) : Cell() {

    var north: CartesianCell? = null
    var south: CartesianCell? = null
    var east: CartesianCell? = null
    var west: CartesianCell? = null

    data class Coordinates(
        val withWallInset: Boolean,
        val withBackInset: Boolean,
        val x1: Int,
        val xw2: Int,
        val xb2: Int,
        val xw3: Int,
        val xb3: Int,
        val x4: Int,
        val y1: Int,
        val yw2: Int,
        val yb2: Int,
        val yw3: Int,
        val yb3: Int,
        val y4: Int
    )

    private lateinit var c: Coordinates

    fun prepareCoordinates(cellSize: Int, wallInset: Int, backInset: Int) {
        val x1 = column * cellSize
        val x4 = x1 + cellSize
        val xw2 = x1 + wallInset
        val xb2 = x1 + backInset
        val xw3 = x4 - wallInset
        val xb3 = x4 - backInset

        val y1 = row * cellSize
        val y4 = y1 + cellSize
        val yw2 = y1 + wallInset
        val yb2 = y1 + backInset
        val yw3 = y4 - wallInset
        val yb3 = y4 - backInset

        c = Coordinates(wallInset > 0, backInset > 0, x1, xw2, xb2, xw3, xb3, x4, y1, yw2, yb2, yw3, yb3, y4)
    }

    override fun neighbors(): List<CartesianCell> {
        return arrayListOf(north, south, east, west).filterNotNull()
    }

    override fun drawBackground(g: Graphics) {
        if (c.withBackInset) {
            g.fillRect(c.xb2, c.yb2, c.xb3-c.xb2, c.yb3-c.yb2)
            if (isLinked(north))
                g.fillRect(c.xb2, c.y1, c.xb3-c.xb2, c.yb2-c.y1)
            if (isLinked(south))
                g.fillRect(c.xb2, c.yb3, c.xb3-c.xb2, c.y4-c.yb3)
            if (isLinked(west))
                g.fillRect(c.x1, c.yb2, c.xb2-c.x1, c.yb3-c.yb2)
            if (isLinked(east))
                g.fillRect(c.xb3, c.yb2, c.x4-c.xb3, c.yb3-c.yb2)
        } else {
            g.fillRect(c.x1, c.y1, c.x4 - c.x1, c.y4 - c.y1)
        }
    }

    override fun drawWalls(g: Graphics) {
        if (c.withWallInset) {
            if (isLinked(north)) {
                g.drawLine(c.xw2, c.y1, c.xw2, c.yw2)
                g.drawLine(c.xw3, c.y1, c.xw3, c.yw2)
            } else {
                g.drawLine(c.xw2, c.yw2, c.xw3, c.yw2)
            }
            if (isLinked(south)) {
                g.drawLine(c.xw2, c.yw3, c.xw2, c.y4)
                g.drawLine(c.xw3, c.yw3, c.xw3, c.y4)
            } else {
                g.drawLine(c.xw2, c.yw3, c.xw3, c.yw3)
            }
            if (isLinked(west)) {
                g.drawLine(c.x1, c.yw2, c.xw2, c.yw2)
                g.drawLine(c.x1, c.yw3, c.xw2, c.yw3)
            } else {
                g.drawLine(c.xw2, c.yw2, c.xw2, c.yw3)
            }
            if (isLinked(east)) {
                g.drawLine(c.xw3, c.yw2, c.x4, c.yw2)
                g.drawLine(c.xw3, c.yw3, c.x4, c.yw3)
            } else {
                g.drawLine(c.xw3, c.yw2, c.xw3, c.yw3)
            }
        } else {
            if (north == null)
                g.drawLine(c.x1, c.y1, c.x4, c.y1)
            if (west == null)
                g.drawLine(c.x1, c.y1, c.x1, c.y4)
            if (!isLinked(east))
                g.drawLine(c.x4, c.y1, c.x4, c.y4)
            if (!isLinked(south))
                g.drawLine(c.x1, c.y4, c.x4, c.y4)
        }
    }
}
