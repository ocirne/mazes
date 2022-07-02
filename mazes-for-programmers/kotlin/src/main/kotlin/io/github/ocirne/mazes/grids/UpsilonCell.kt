package io.github.ocirne.mazes.grids

import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.colorization.Strokes
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Point
import java.awt.Polygon

class UpsilonCell(val row: Int, val column: Int) : Cell() {

    var north: UpsilonCell? = null
    var northeast: UpsilonCell? = null
    var east: UpsilonCell? = null
    var southeast: UpsilonCell? = null
    var south: UpsilonCell? = null
    var southwest: UpsilonCell? = null
    var west: UpsilonCell? = null
    var northwest: UpsilonCell? = null

    override fun neighbors(): List<UpsilonCell> {
        return arrayListOf(north, northeast, east, southeast, south, southwest, west, northwest).filterNotNull()
    }

    fun isOctogon(): Boolean {
        // TODO explore alternative: grid of a Square and an Octogon
        return (row + column) % 2 == 0
    }

    data class Coordinates(
        val p0: Point,
        val p1: Point,
        val p2: Point,
        val p3: Point,
        val p4: Point,
        val p5: Point,
        val p6: Point,
        val p7: Point
    )

    private lateinit var c: Coordinates

    fun prepareCoordinates(correctedSize: Double, halfCSize: Double, aSize: Double) {
        val cx = (column + 1) * correctedSize
        val cy = (row + 1) * correctedSize

        val x0 = (cx - halfCSize - aSize).toInt()
        val x1 = (cx - halfCSize).toInt()
        val x2 = (cx + halfCSize).toInt()
        val x3 = (cx + halfCSize + aSize).toInt()

        val y0 = (cy - halfCSize - aSize).toInt()
        val y1 = (cy - halfCSize).toInt()
        val y2 = (cy + halfCSize).toInt()
        val y3 = (cy + halfCSize + aSize).toInt()

        val p0: Point
        val p1: Point
        val p2: Point
        val p3: Point
        val p4: Point
        val p5: Point
        val p6: Point
        val p7: Point

        if (isOctogon()) {
            p0 = Point(x0, y2)
            p1 = Point(x0, y1)
            p2 = Point(x1, y0)
            p3 = Point(x2, y0)
            p4 = Point(x3, y1)
            p5 = Point(x3, y2)
            p6 = Point(x2, y3)
            p7 = Point(x1, y3)
        } else {
            p0 = Point(x1, y2)
            p7 = p0
            p2 = Point(x1, y1)
            p1 = p2
            p4 = Point(x2, y1)
            p3 = p4
            p6 = Point(x2, y2)
            p5 = p6
        }
        c = Coordinates(p0, p1, p2, p3, p4, p5, p6, p7)
    }

    override fun drawBackground(g: Graphics2D, colorization: Colorization) {
        g.color = colorization.valueFor(this)
        val p = Polygon()
        if (isOctogon()) {
            p.addPoint(c.p0.x, c.p0.y)
            p.addPoint(c.p1.x, c.p1.y)
            p.addPoint(c.p2.x, c.p2.y)
            p.addPoint(c.p3.x, c.p3.y)
            p.addPoint(c.p4.x, c.p4.y)
            p.addPoint(c.p5.x, c.p5.y)
            p.addPoint(c.p6.x, c.p6.y)
            p.addPoint(c.p7.x, c.p7.y)
        } else {
            p.addPoint(c.p0.x, c.p0.y)
            p.addPoint(c.p2.x, c.p2.y)
            p.addPoint(c.p4.x, c.p4.y)
            p.addPoint(c.p6.x, c.p6.y)
        }
        g.fillPolygon(p)

    }

    private fun drawline(g: Graphics, p1: Point, p2: Point) {
        g.drawLine(p1.x, p1.y, p2.x, p2.y)
    }

    override fun drawWalls(g: Graphics2D, colorization: Colorization, strokes: Strokes) {
        g.color = colorization.valueFor(this)
        g.stroke = strokes.getBasicWall()
        if (west == null)
            drawline(g, c.p0, c.p1)
        if (north == null)
            drawline(g, c.p2, c.p3)
        if (!isLinked(east))
            drawline(g, c.p4, c.p5)
        if (!isLinked(south))
            drawline(g, c.p6, c.p7)

        if (isOctogon()) {
            if (northwest == null)
                drawline(g, c.p1, c.p2)
            if (!isLinked(northeast))
                drawline(g, c.p3, c.p4)
            if (!isLinked(southeast))
                drawline(g, c.p5, c.p6)
            if (southwest == null)
                drawline(g, c.p7, c.p0)
        }
    }
}
