package io.github.ocirne.mazes.grids

import io.github.ocirne.mazes.colorization.Colorization
import java.awt.Graphics2D

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

    fun canTunnelCw(): Boolean {
        return outward.size == 1 && cw != null && cw!!.cw != null && isHorizontalPassage(cw!!)
    }

    fun canTunnelCcw(): Boolean {
        return outward.size == 1 && ccw != null && ccw!!.ccw != null && isHorizontalPassage(ccw!!)
    }

    fun canTunnelInward(): Boolean {
        return outward.size == 1 && inward != null && inward!!.inward != null && isVerticalPassage(inward!!)
    }

    fun canTunnelOutward(): Boolean {
        return outward.size == 1 && outward[0].outward.size == 1 && isVerticalPassage(outward[0])
    }

    fun isHorizontalPassage(cell: PolarCell): Boolean {
        return cell.isLinked(cell.inward) && cell.isLinked(cell.outward[0]) && !cell.isLinked(cell.cw) && !cell.isLinked(cell.ccw)
    }

    fun isVerticalPassage(cell: PolarCell): Boolean {
        return cell.isLinked(cell.cw) && cell.isLinked(cell.ccw) && !cell.isLinked(cell.inward) && !cell.isLinked(cell.outward[0])
    }

    override fun link(cell: Cell, bidi: Boolean) {
        cell as PolarCell
        val neighbor = if (cw != null && cw == cell.ccw) {
            cw
        } else if (ccw != null && ccw == cell.cw) {
            ccw
        } else if (inward != null && inward == cell.outward) {
            inward
        } else if (outward != null && outward.size == 1 && outward == cell.inward) {
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

class PolarUnderCell(overCell: PolarOverCell) : PolarCell(overCell.row, overCell.column) {
    init {
        if (overCell.isHorizontalPassage(overCell)) {
            cw = overCell.cw
            overCell.cw!!.ccw = this
            ccw = overCell.ccw
            overCell.ccw!!.cw = this

            link(cw!!)
            link(ccw!!)
        } else {
            inward = overCell.inward
            overCell.inward!!.outward[0] = this
            outward[0] = overCell.outward[0]
            overCell.outward[0].inward = this

            link(inward!!)
            link(outward[0])
        }
    }

    fun horizontalPassage(): Boolean {
        return inward != null || outward != null
    }

    fun verticalPassage(): Boolean {
        return cw != null || ccw != null
    }

    override fun drawWalls(g: Graphics2D, colorization: Colorization) {
        g.color = colorization.colorFor(this)
        if (verticalPassage()) {
//            g.drawLine(c.x2, c.y1, c.x2, c.y2)
//            g.drawLine(c.x3, c.y1, c.x3, c.y2)
//            g.drawLine(c.x2, c.y3, c.x2, c.y4)
//            g.drawLine(c.x3, c.y3, c.x3, c.y4)
//        } else {
//            g.drawLine(c.x1, c.y2, c.x2, c.y2)
//            g.drawLine(c.x1, c.y3, c.x2, c.y3)
//            g.drawLine(c.x3, c.y2, c.x4, c.y2)
//            g.drawLine(c.x3, c.y3, c.x4, c.y3)
        }
    }
}
