package io.github.ocirne.mazes.grids

import io.github.ocirne.mazes.colorization.Colorization
import java.awt.Graphics
import kotlin.math.*

class PolarCell(val row: Int, val column: Int) : Cell() {

    var cw: PolarCell? = null
    var ccw: PolarCell? = null
    var inward: PolarCell? = null
    val outward: MutableList<PolarCell> = mutableListOf()

    data class Coordinates(
        val center: Int,
        val cellSize: Int,
        val innerRadius: Int,
        val outerRadius: Int,
        val thetaDeg: Double,
        val ax: Int,
        val ay: Int,
        val bx: Int,
        val by: Int,
        val cx: Int,
        val cy: Int,
        val dx: Int,
        val dy: Int
    )

    private lateinit var c: Coordinates

    fun prepareCoordinates(grid: PolarGrid, center: Int, cellSize: Int) {
        val thetaRad = 2 * PI / grid[row]!!.size
        val thetaDeg = 360.0 / grid[row]!!.size
        val innerRadius = row * cellSize
        val outerRadius = (row + 1) * cellSize
        val thetaCcw = column * thetaRad
        val thetaCw = (column + 1) * thetaRad

        val ax = (center + innerRadius * cos(thetaCcw)).toInt()
        val ay = (center - innerRadius * sin(thetaCcw)).toInt()
        val bx = (center + outerRadius * cos(thetaCcw)).toInt()
        val by = (center - outerRadius * sin(thetaCcw)).toInt()
        val cx = (center + innerRadius * cos(thetaCw)).toInt()
        val cy = (center - innerRadius * sin(thetaCw)).toInt()
        val dx = (center + outerRadius * cos(thetaCw)).toInt()
        val dy = (center - outerRadius * sin(thetaCw)).toInt()
        c = Coordinates(center, cellSize, innerRadius, outerRadius, thetaDeg, ax, ay, bx, by, cx, cy, dx, dy)
    }

    override fun neighbors(): List<PolarCell> {
        return arrayListOf(cw, ccw, inward).filterNotNull() + outward
    }

    override fun drawBackground(g: Graphics, colorization: Colorization) {
        g.color = colorization.colorForBackground(this)
        val cw = column * c.thetaDeg
        for (x in 0..c.cellSize) {
            val xy = c.center-(c.innerRadius+x)
            val wh = 2*(c.innerRadius+x)
            g.drawArc(xy, xy, wh, wh, cw.toInt(), c.thetaDeg.roundToInt())
        }
    }

    override fun drawWalls(g: Graphics, colorization: Colorization) {
        g.color = colorization.colorForWall(this)
        if (!isLinked(inward)) {
            val cw = column * c.thetaDeg
            val xy = c.center - (c.innerRadius)
            val wh = 2 * (c.innerRadius)
            g.drawArc(xy, xy, wh, wh, cw.toInt(), c.thetaDeg.roundToInt())
        }
        if (!isLinked(cw))
            g.drawLine(c.cx, c.cy, c.dx, c.dy)
    }
}
