package io.github.ocirne.mazes.grids

import io.github.ocirne.mazes.colorization.Colorization
import java.awt.Graphics2D
import java.awt.geom.Arc2D
import java.awt.geom.Line2D
import kotlin.math.*


class PolarCell(val row: Int, val column: Int) : Cell() {

    var cw: PolarCell? = null
    var ccw: PolarCell? = null
    var inward: PolarCell? = null
    val outward: MutableList<PolarCell> = mutableListOf()

    data class Coordinates(
        val center: Int,
        val cellSize: Int,
        val innerRadius: Double,
        val outerRadius: Double,
        val thetaDeg: Double,
        val ax: Double,
        val ay: Double,
        val bx: Double,
        val by: Double,
        val cx: Double,
        val cy: Double,
        val dx: Double,
        val dy: Double
    )

    private lateinit var c: Coordinates

    fun prepareCoordinates(grid: PolarGrid, center: Int, cellSize: Int) {
        val thetaRad = 2 * PI / grid[row]!!.size
        val thetaDeg = 360.0 / grid[row]!!.size
        val innerRadius = (row * cellSize).toDouble()
        val outerRadius = ((row + 1) * cellSize).toDouble()
        val thetaCcw = column * thetaRad
        val thetaCw = (column + 1) * thetaRad

        val ax = center + innerRadius * cos(thetaCcw)
        val ay = center - innerRadius * sin(thetaCcw)
        val bx = center + outerRadius * cos(thetaCcw)
        val by = center - outerRadius * sin(thetaCcw)
        val cx = center + innerRadius * cos(thetaCw)
        val cy = center - innerRadius * sin(thetaCw)
        val dx = center + outerRadius * cos(thetaCw)
        val dy = center - outerRadius * sin(thetaCw)
        c = Coordinates(center, cellSize, innerRadius, outerRadius, thetaDeg, ax, ay, bx, by, cx, cy, dx, dy)
    }

    override fun neighbors(): List<PolarCell> {
        return arrayListOf(cw, ccw, inward).filterNotNull() + outward
    }

    override fun drawBackground(g: Graphics2D, colorization: Colorization) {
        g.color = colorization.colorForBackground(this)
        val cw = column * c.thetaDeg
        for (x in 0..c.cellSize) {
            val xy = c.center-(c.innerRadius+x)
            val wh = 2*(c.innerRadius+x)
            g.draw(Arc2D.Double(xy, xy, wh, wh, cw, c.thetaDeg, Arc2D.OPEN))
        }
    }

    override fun drawWalls(g: Graphics2D, colorization: Colorization) {
        g.color = colorization.colorForWall(this)
        if (!isLinked(inward)) {
            val cw = column * c.thetaDeg
            val xy = c.center - (c.innerRadius)
            val wh = 2 * (c.innerRadius)
            g.draw(Arc2D.Double(xy, xy, wh, wh, cw, c.thetaDeg, Arc2D.OPEN))
        }
        if (!isLinked(cw))
            g.draw(Line2D.Double(c.cx, c.cy, c.dx, c.dy))
    }
}
