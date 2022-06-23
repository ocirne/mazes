package io.github.ocirne.mazes.grids

import io.github.ocirne.mazes.colorization.Colorization
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D
import java.awt.geom.Arc2D
import java.awt.geom.Area
import java.awt.geom.Line2D
import java.awt.geom.Point2D
import java.lang.Math.toDegrees
import kotlin.math.*


class PolarCell(val row: Int, val column: Int) : Cell() {

    var cw: PolarCell? = null
    var ccw: PolarCell? = null
    var inward: PolarCell? = null
    val outward: MutableList<PolarCell> = mutableListOf()

    data class Coordinates(
        val withInset: Boolean,
        val center: Int,
        val cellSize: Int,
        val r1: Double,
        val r2: Double,
        val r3: Double,
        val r4: Double,
        val theta: Double,
        val a: Point2D,
        val aI: Point2D,
        val aL: Point2D,
        val b: Point2D,
        val bL: Point2D,
        val bO: Point2D,
        val c: Point2D,
        val cI: Point2D,
        val cR: Point2D,
        val d: Point2D,
        val dO: Point2D,
        val dR: Point2D,
        val e: Point2D,
        val eO: Point2D,
        val f: Point2D,
        val fO: Point2D,
        val thetaCw: Double,
        val thetaCcw: Double,
        val thetaInset1: Double,
        val thetaInset2: Double,
        val thetaInset3: Double,
        val thetaInset4: Double,
    )

    private lateinit var c: Coordinates

    private fun toPolar(center: Int, radius: Double, angle: Double): Point2D {
        return Point2D.Double(center + radius * cos(angle), center - radius * sin(angle))
    }

    fun prepareCoordinates(grid: PolarGrid, center: Int, cellSize: Int, inset: Double=0.0) {
        val theta = 2 * PI / grid[row]!!.size

        val r1 = (row * cellSize).toDouble()
        val r4 = ((row + 1) * cellSize).toDouble()
        // TODO backInset muss ähnlich behandelt werden
        val radiusInset = inset * cellSize
        val thetaInset = radiusInset * 2*PI / 6
        val r2 = r1 + radiusInset
        val r3 = r4 - radiusInset

        val thetaCw = column * theta
        val thetaCcw = (column + 1) * theta
        val thetaInset1 = thetaInset / r1
        val thetaInset2 = thetaInset / r2
        val thetaInset3 = thetaInset / r3
        val thetaInset4 = thetaInset / r4

        val aa = toPolar(center, r2, thetaCcw - thetaInset2)
        val aI = toPolar(center, r1, thetaCcw - thetaInset1)
        val aL = toPolar(center, r2, thetaCcw)
        val bb = toPolar(center, r3, thetaCcw - thetaInset3)
        val bL = toPolar(center, r3, thetaCcw)
        val bO = toPolar(center, r4, thetaCcw - thetaInset4)
        val cc = toPolar(center, r2, thetaCw + thetaInset2)
        val cI = toPolar(center, r1, thetaCw + thetaInset1)
        val cR = toPolar(center, r2, thetaCw)
        val dd = toPolar(center, r3, thetaCw + thetaInset3)
        val dO = toPolar(center, r4, thetaCw + thetaInset4)
        val dR = toPolar(center, r3, thetaCw)

        val ee = toPolar(center, r3, thetaCw + 0.5 * theta + thetaInset3)
        val eO = toPolar(center, r4, thetaCw + 0.5 * theta + thetaInset4)
        val ff = toPolar(center, r3, thetaCw + 0.5 * theta - thetaInset3)
        val fO = toPolar(center, r4, thetaCw + 0.5 * theta - thetaInset4)

        c = Coordinates(inset > 0, center, cellSize, r1, r2, r3, r4, theta,
            aa, aI, aL, bb, bL, bO, cc, cI, cR, dd, dO, dR, ee, eO, ff, fO,
             thetaCw,
         thetaCcw,
         thetaInset1,
         thetaInset2,
         thetaInset3,
         thetaInset4
        )
    }

    override fun neighbors(): List<PolarCell> {
        return arrayListOf(cw, ccw, inward).filterNotNull() + outward
    }

    private fun drawArc(g: Graphics2D, r: Double, start: Double, extent: Double) {
        val xy = c.center - r
        val wh = 2 * r
        g.draw(Arc2D.Double(xy, xy, wh, wh, start, extent, Arc2D.OPEN))
    }

    private fun drawFoo(g: Graphics2D, r1: Double, r2: Double, start: Double, extent: Double) {
        val xy1 = c.center - r2
        val wh1 = 2.0 * r2
        val outerPie = Area(Arc2D.Double(xy1, xy1, wh1, wh1, toDegrees(start), toDegrees(extent), Arc2D.PIE))
        val xy2 = c.center - r1
        val wh2 = 2.0 * r1
        val innerPie = Area(Arc2D.Double(xy2, xy2, wh2, wh2, toDegrees(start), toDegrees(extent), Arc2D.PIE))
        outerPie.subtract(innerPie)
        g.fill(outerPie)
    }

    override fun drawBackground(g: Graphics2D, colorization: Colorization) {
        g.color = colorization.colorFor(this)
        if (c.withInset) {
            drawFoo(g, c.r2, c.r3, c.thetaCw + c.thetaInset3, c.theta - 2 * c.thetaInset3)
            if (isLinked(ccw) && colorization.isColoredCell(ccw))
                drawFoo(g, c.r2, c.r3, c.thetaCw, c.thetaInset2)
            if (isLinked(cw) && colorization.isColoredCell(cw))
                drawFoo(g, c.r2, c.r3, c.thetaCcw - c.thetaInset2, c.thetaInset2)
            if (isLinked(inward) && colorization.isColoredCell(inward))
                drawFoo(g, c.r1, c.r2, c.thetaCw + c.thetaInset2, c.theta - 2 * c.thetaInset2)

            if (outward.isEmpty()) {
                // ignore
            } else if (outward.size == 1) {
                if (isLinked(outward[0]) && colorization.isColoredCell(outward[0])) {
                    drawFoo(g, c.r3, c.r4, c.thetaCw + c.thetaInset4, c.theta - 2 * c.thetaInset4)
                }
            } else if (outward.size == 2) {
                if (isLinked(outward[0]) && colorization.isColoredCell(outward[0])) {
                    drawFoo(g, c.r3, c.r4, c.thetaCw + c.thetaInset4, 0.5 * c.theta - 2 * c.thetaInset4)
                }
                if (isLinked(outward[1]) && colorization.isColoredCell(outward[1])) {
                    drawFoo(g, c.r3, c.r4, c.thetaCw + 0.5 * c.theta + c.thetaInset4, 0.5 * c.theta - 2 * c.thetaInset4)
                }
            } else {
              //  throw NotImplementedError()
            }

        } else {
            drawFoo(g, c.r1, c.r4, c.thetaCw, c.theta)
        }
    }

    /**            cw
     *         al     bl
            ai  a --- b  bo
                |     |
                |     e  eO
     inward     |     |        outward
                |     f  fO
                |     |
            ci  c --- d  do
               cr     dr
                   ccw
     */
    override fun drawWalls(g: Graphics2D, colorization: Colorization) {
        g.color = colorization.colorFor(this)
        g.stroke = BasicStroke(5.0f)
        if (c.withInset) {
            if (isLinked(cw)) {
                drawArc(g, c.r2, toDegrees(c.thetaCcw - c.thetaInset2), toDegrees(c.thetaInset2))
                drawArc(g, c.r3, toDegrees(c.thetaCcw - c.thetaInset3), toDegrees(c.thetaInset3))
            } else {
                g.draw(Line2D.Double(c.a, c.b))
            }
            if (isLinked(ccw)) {
                drawArc(g, c.r2, toDegrees(c.thetaCw), toDegrees(c.thetaInset2))
                drawArc(g, c.r3, toDegrees(c.thetaCw), toDegrees(c.thetaInset3))
            } else {
                g.draw(Line2D.Double(c.c, c.d))
            }
            if (isLinked(inward)) {
                g.draw(Line2D.Double(c.a, c.aI))
                g.draw(Line2D.Double(c.c, c.cI))
            } else {
                drawArc(g, c.r2, toDegrees(c.thetaCw + c.thetaInset2), toDegrees(c.theta - (2*c.thetaInset2)))
            }
            if (outward.isEmpty()) {
                // Outer rim
                val thetaR3 = c.theta * c.r3 / c.r1
                drawArc(g, c.r3, toDegrees(c.thetaCw), toDegrees(thetaR3))
            } else if (outward.size == 1) {
                if (isLinked(outward[0])) {
                    g.draw(Line2D.Double(c.b, c.bO))
                    g.draw(Line2D.Double(c.d, c.dO))
                } else {
                    drawArc(g, c.r3, toDegrees(c.thetaCw + c.thetaInset3), toDegrees(c.theta - (2*c.thetaInset3)))
                }
            } else if (outward.size == 2) {
                if (isLinked(outward[0])) {
                    g.draw(Line2D.Double(c.d, c.dO))
                    g.draw(Line2D.Double(c.f, c.fO))
                } else {
                    drawArc(g, c.r3, toDegrees(c.thetaCw + c.thetaInset3),
                        toDegrees(0.5 * c.theta - (2*c.thetaInset3)))
                }
                drawArc(g, c.r3, toDegrees(c.thetaCw + 0.5 * c.theta - c.thetaInset3),
                    toDegrees(2*c.thetaInset3))
                if (isLinked(outward[1])) {
                    g.draw(Line2D.Double(c.b, c.bO))
                    g.draw(Line2D.Double(c.e, c.eO))
                } else {
                    drawArc(g, c.r3, toDegrees(c.thetaCw + 0.5 * c.theta + c.thetaInset3),
                                     toDegrees(0.5 * c.theta - (2*c.thetaInset3)))
                }
            } else {
                throw NotImplementedError()
            }
        } else {
            if (!isLinked(inward)) {
                drawArc(g, c.r1, toDegrees(c.thetaCw), toDegrees(c.theta))
            }
            if (!isLinked(cw))
                g.draw(Line2D.Double(c.a, c.b))
        }
    }

    fun drawPath(g: Graphics2D, colorization: Colorization, grid: PolarGrid, center: Int, cellSize: Int) {
        g.color = colorization.colorFor(this)
        if (g.color == Color.WHITE) {
            return
        }
        val m = middle(grid, center, cellSize)
        for (other in links()) {
            if (!colorization.isColoredCell(other)) {
                return
            }
            other as PolarCell
            val o = other.middle(grid, center, cellSize)
            g.color = Color.MAGENTA
            g.stroke = BasicStroke(10.0f)
            g.draw(Line2D.Double(m, o))
        }
    }

    private fun middle(grid: PolarGrid, center: Int, cellSize: Int): Point2D {
        val r = (row +0.5) * cellSize
        val theta = 2 * PI / grid[row]!!.size
        val t = (column+0.5) * theta
        return toPolar(center, r, t)
    }

    fun drawMarker(g: Graphics2D, colorization: Colorization, grid: PolarGrid, center: Int, cellSize: Int) {
        val marker = colorization.marker(this) ?: return
        val m = middle(grid, center, cellSize)
        val size = cellSize / 4
        g.color = Color.RED
        g.fillOval(m.x.toInt() - size, m.y.toInt()-size, 2*size, 2*size)
        g.color = Color.WHITE
        g.setFont(Font("Sans", Font.BOLD or Font.CENTER_BASELINE, size))
        g.drawString(marker, m.x.toFloat() - size / 2, m.y.toFloat() + size / 3);
    }
}
