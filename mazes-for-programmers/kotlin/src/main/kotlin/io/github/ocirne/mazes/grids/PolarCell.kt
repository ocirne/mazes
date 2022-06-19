package io.github.ocirne.mazes.grids

import java.awt.Graphics
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class PolarCell(val row: Int, val column: Int) : Cell() {

    var cw: PolarCell? = null
    var ccw: PolarCell? = null
    var inward: PolarCell? = null
    val outward: MutableList<PolarCell> = mutableListOf()

    data class Coordinates(val ax: Int, val ay: Int, val cx: Int, val cy: Int, val dx: Int, val dy: Int)

    lateinit var c: Coordinates

    fun prepareCoordinates(cellSize: Int, center: Int, grid: PolarGrid) {
        val theta = 2 * PI / grid[row]!!.size
        val innerRadius = row * cellSize
        val outerRadius = (row + 1) * cellSize
        val thetaCcw = column * theta
        val thetaCw = (column + 1) * theta

        val ax = center + (innerRadius * cos(thetaCcw)).toInt()
        val ay = center + (innerRadius * sin(thetaCcw)).toInt()
        val cx = center + (innerRadius * cos(thetaCw)).toInt()
        val cy = center + (innerRadius * sin(thetaCw)).toInt()
        val dx = center + (outerRadius * cos(thetaCw)).toInt()
        val dy = center + (outerRadius * sin(thetaCw)).toInt()
        c = Coordinates(ax, ay, cx, cy, dx, dy)
    }

    override fun neighbors(): List<PolarCell> {
        return arrayListOf(cw, ccw, inward).filterNotNull() + outward
    }

    override fun drawBackground(g: Graphics) {
        TODO("Not yet implemented")
    }

    override fun drawWalls(g: Graphics) {
        if (!isLinked(inward))
            g.drawLine(c.ax, c.ay, c.cx, c.cy)
        if (!isLinked(cw))
            g.drawLine(c.cx, c.cy, c.dx, c.dy)
    }
}
