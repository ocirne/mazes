package io.github.ocirne.mazes.grids

import io.github.ocirne.mazes.colorization.Colorization
import java.awt.Graphics
import java.awt.Polygon

class TriangleCell(val row: Int, val column: Int) : Cell() {

    var east: TriangleCell? = null
    var west: TriangleCell? = null
    var north: TriangleCell? = null
    var south: TriangleCell? = null

    override fun neighbors(): List<TriangleCell> {
        return arrayListOf(west, east, if (isUpright()) south else north).filterNotNull()
    }

    fun isUpright(): Boolean {
        return (row + column) % 2 == 0
    }

    data class Coordinates(
        val westX: Int,
        val midX: Int,
        val eastX: Int,
        val apexY: Int,
        val baseY: Int
    )

    private lateinit var c: Coordinates

    fun prepareCoordinates(halfWidth: Double, height: Double, halfHeight: Double) {
        val cx = halfWidth + column * halfWidth
        val cy = halfHeight + row * height

        val westX = (cx - halfWidth).toInt()
        val midX = cx.toInt()
        val eastX = (cx + halfWidth).toInt()

        val apexY: Int
        val baseY: Int

        if (isUpright()) {
            apexY = (cy - halfHeight).toInt()
            baseY = (cy + halfHeight).toInt()
        } else {
            apexY = (cy + halfHeight).toInt()
            baseY = (cy - halfHeight).toInt()
        }
        c = Coordinates(westX, midX, eastX, apexY, baseY)
    }

    override fun drawBackground(g: Graphics, colorization: Colorization) {
        g.color = colorization.colorForBackground(this)
        val p = Polygon()
        p.addPoint(c.westX, c.baseY)
        p.addPoint(c.midX, c.apexY)
        p.addPoint(c.eastX, c.baseY)
        g.fillPolygon(p)
    }

    override fun drawWalls(g: Graphics, colorization: Colorization) {
        g.color = colorization.colorForWall(this)
        if (west == null)
            g.drawLine(c.westX, c.baseY, c.midX, c.apexY)
        if (!isLinked(east))
            g.drawLine(c.eastX, c.baseY, c.midX, c.apexY)
        val noSouth = isUpright() && south == null
        val notLinked = !isUpright() && !isLinked(north)
        if (noSouth || notLinked)
            g.drawLine(c.eastX, c.baseY, c.westX, c.baseY)

    }
}
