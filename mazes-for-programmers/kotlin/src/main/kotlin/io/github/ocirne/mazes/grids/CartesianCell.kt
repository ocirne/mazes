package io.github.ocirne.mazes.grids

import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.colorization.Strokes
import java.awt.BasicStroke
import java.awt.Graphics2D

open class CartesianCell(val row: Int, val column: Int) : Cell() {

    var north: CartesianCell? = null
    var south: CartesianCell? = null
    var east: CartesianCell? = null
    var west: CartesianCell? = null

    data class Coordinates(
        val withInset: Boolean,
        val x1: Int,
        val x2: Int,
        val x3: Int,
        val x4: Int,
        val y1: Int,
        val y2: Int,
        val y3: Int,
        val y4: Int
    )

    lateinit var c: Coordinates

    fun prepareCoordinates(cellSize: Int, inset: Int) {
        val x1 = column * cellSize
        val x4 = x1 + cellSize
        val x2 = x1 + inset
        val x3 = x4 - inset

        val y1 = row * cellSize
        val y4 = y1 + cellSize
        val y2 = y1 + inset
        val y3 = y4 - inset

        c = Coordinates(inset > 0, x1, x2, x3, x4, y1, y2, y3, y4)
    }

    override fun neighbors(): List<CartesianCell> {
        return arrayListOf(north, south, east, west).filterNotNull()
    }

    override fun drawBackground(g: Graphics2D, colorization: Colorization) {
        g.color = colorization.valueFor(this)
        if (c.withInset) {
            if (this !is UnderCell) {
                g.fillRect(c.x2, c.y2, c.x3 - c.x2, c.y3 - c.y2)
            }
            if (isLinked(north) && colorization.isValuedCell(north))
                g.fillRect(c.x2, c.y1, c.x3-c.x2, c.y2-c.y1)
            if (isLinked(south) && colorization.isValuedCell(south))
                g.fillRect(c.x2, c.y3, c.x3-c.x2, c.y4-c.y3)
            if (isLinked(west) && colorization.isValuedCell(west))
                g.fillRect(c.x1, c.y2, c.x2-c.x1, c.y3-c.y2)
            if (isLinked(east) && colorization.isValuedCell(east))
                g.fillRect(c.x3, c.y2, c.x4-c.x3, c.y3-c.y2)
        } else {
            if (this !is UnderCell) {
                g.fillRect(c.x1, c.y1, c.x4 - c.x1, c.y4 - c.y1)
            }
        }
    }

    override fun drawWalls(g: Graphics2D, colorization: Colorization, strokes: Strokes) {
        g.color = colorization.valueFor(this)
        g.stroke = strokes.getBasicWall()
        if (c.withInset) {
            if (isLinked(north)) {
                g.drawLine(c.x2, c.y1, c.x2, c.y2)
                g.drawLine(c.x3, c.y1, c.x3, c.y2)
            } else {
                g.drawLine(c.x2, c.y2, c.x3, c.y2)
            }
            if (isLinked(south)) {
                g.drawLine(c.x2, c.y3, c.x2, c.y4)
                g.drawLine(c.x3, c.y3, c.x3, c.y4)
            } else {
                g.drawLine(c.x2, c.y3, c.x3, c.y3)
            }
            if (isLinked(west)) {
                g.drawLine(c.x1, c.y2, c.x2, c.y2)
                g.drawLine(c.x1, c.y3, c.x2, c.y3)
            } else {
                g.drawLine(c.x2, c.y2, c.x2, c.y3)
            }
            if (isLinked(east)) {
                g.drawLine(c.x3, c.y2, c.x4, c.y2)
                g.drawLine(c.x3, c.y3, c.x4, c.y3)
            } else {
                g.drawLine(c.x3, c.y2, c.x3, c.y3)
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
