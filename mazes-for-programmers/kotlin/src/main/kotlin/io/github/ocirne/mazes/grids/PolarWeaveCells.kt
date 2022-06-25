package io.github.ocirne.mazes.grids

import io.github.ocirne.mazes.colorization.Colorization
import java.awt.BasicStroke
import java.awt.Graphics2D
import java.awt.geom.Line2D

class PolarOverCell(row: Int, column: Int, val grid: PolarWeaveGrid) : PolarCell(row, column) {

    override fun neighbors(): List<PolarCell> {
        val result = super.neighbors().toMutableList()
        if (canTunnelCw())
            result.add(cw!!.cw!!)
        if (canTunnelCcw())
            result.add(ccw!!.ccw!!)
        if (canTunnelInward())
            result.add(inward!!.inward!!)
        if (canTunnelOutward())
            result.add(outward[0].outward[0])
        return result.toList()
    }

    private fun canTunnelCw(): Boolean {
        return outward.size == 1 && cw != null && cw!!.cw != null && isHorizontalPassage(cw!!)
    }

    private fun canTunnelCcw(): Boolean {
        return outward.size == 1 && ccw != null && ccw!!.ccw != null && isHorizontalPassage(ccw!!)
    }

    private fun canTunnelInward(): Boolean {
        return outward.size == 1 && inward != null && inward!!.inward != null && isVerticalPassage(inward!!)
    }

    private fun canTunnelOutward(): Boolean {
        return outward.size == 1 && outward[0].outward.size == 1 && isVerticalPassage(outward[0])
    }

    fun isHorizontalPassage(cell: PolarCell): Boolean {
        return cell.outward.size == 1 && cell.isLinked(cell.inward) && cell.isLinked(cell.outward[0]) && !cell.isLinked(cell.cw) && !cell.isLinked(cell.ccw)
    }

    fun isVerticalPassage(cell: PolarCell): Boolean {
        return cell.outward.size == 1 && cell.isLinked(cell.cw) && cell.isLinked(cell.ccw) && !cell.isLinked(cell.inward) && !cell.isLinked(cell.outward[0])
    }

    override fun link(cell: Cell, bidi: Boolean) {
        cell as PolarCell
        val neighbor = if (cw != null && cw == cell.ccw) {
            cw
        } else if (ccw != null && ccw == cell.cw) {
            ccw
        } else if (outward.size == 1 && inward != null && cell.outward.size == 1 && inward == cell.outward[0]) {
            inward
        } else if (outward.size == 1 && outward[0] == cell.inward) {
            outward[0]
        } else {
            null
        }
        if (neighbor != null) {
            grid.tunnelUnder(neighbor as PolarOverCell)
        } else {
            super.link(cell, bidi)
        }
    }
}

class PolarUnderCell(private val overCell: PolarOverCell) : PolarCell(overCell.row, overCell.column) {
    init {
        if (overCell.isHorizontalPassage(overCell)) {
            cw = overCell.cw
            overCell.cw!!.ccw = this
            ccw = overCell.ccw
            overCell.ccw!!.cw = this

            link(cw!!)
            link(ccw!!)
        } else if (overCell.isVerticalPassage(overCell)) {
            inward = overCell.inward
            overCell.inward!!.outward[0] = this
            outward.add(overCell.outward[0])
            overCell.outward[0].inward = this

            link(inward!!)
            link(outward[0])
        }
    }

    fun isHorizontalPassage(): Boolean {
        return inward != null || outward.size == 1
    }

    private fun isVerticalPassage(): Boolean {
        return cw != null || ccw != null
    }

    override fun drawWalls(g: Graphics2D, colorization: Colorization) {
        g.color = colorization.valueFor(this)
        val c = overCell.c
        if (isVerticalPassage()) {
            // TODO das müssten wieder arcs sein
            g.stroke = BasicStroke(5.0f)
            g.draw(Line2D.Double(c.a, c.aL))
            g.draw(Line2D.Double(c.b, c.bL))
            g.draw(Line2D.Double(c.c, c.cR))
            g.draw(Line2D.Double(c.d, c.dR))

            g.stroke = BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0.0f, floatArrayOf(9.0f), 0.0f)
            g.draw(Line2D.Double(c.a, c.c))
            g.draw(Line2D.Double(c.b, c.d))
        } else {
            g.stroke = BasicStroke(5.0f)
            g.draw(Line2D.Double(c.a, c.aI))
            g.draw(Line2D.Double(c.b, c.bO))
            g.draw(Line2D.Double(c.c, c.cI))
            g.draw(Line2D.Double(c.d, c.dO))

            g.stroke = BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0.0f, floatArrayOf(9.0f), 0.0f)
            g.draw(Line2D.Double(c.a, c.b))
            g.draw(Line2D.Double(c.c, c.d))
        }
    }
}
