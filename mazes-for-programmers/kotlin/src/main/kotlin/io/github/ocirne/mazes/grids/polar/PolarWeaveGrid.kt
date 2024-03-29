package io.github.ocirne.mazes.grids.polar

import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.colorization.Strokes
import io.github.ocirne.mazes.grids.Cell
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.Line2D
import kotlin.math.PI
import kotlin.math.roundToInt

class PolarWeaveGrid(private val rows: Int): PolarGrid(rows) {

    class PolarWeaveMaze(private val rows: Int) : PolarMaze(rows) {

        private val underCells: MutableList<PolarCell> = mutableListOf()

        init {
            grid = prepareGrid2()
            configureCells()
        }

        private fun prepareGrid2(): Array<Array<PolarCell>> {
            val rings = mutableListOf<Array<PolarCell>>()

            val rowHeight = 1.0 / rows.toFloat()
            rings.add(arrayOf(PolarCell(0, 0)))

            for (row in 1..rows) {
                val radius = row.toFloat() / rows
                val circumference = 2 * PI * radius

                val previousCount = rings[row - 1].size
                val estimatedCellWidth = circumference.toFloat() / previousCount
                val ratio = (estimatedCellWidth / rowHeight).roundToInt()

                val cells = previousCount * ratio
                rings.add(Array(cells) { col -> PolarOverCell(row, col, this) })
            }
            return Array(rows) { row -> rings[row] }
        }

        fun tunnelUnder(overCell: PolarOverCell) {
            val underCell = PolarUnderCell(overCell)
            underCells.add(underCell)
        }

        override fun eachCell(): List<PolarCell> {
            if (underCells == null) {
                return super.eachCell()
            }
            return super.eachCell() + underCells
        }

        override fun size(): Int {
            return super.size() + underCells.size
        }

        fun isUndercell(cell: PolarCell?): Boolean {
            if (cell == null) {
                return false
            }
            return underCells.any { it.row == cell.row && it.column == cell.column }
        }
    }

    class PolarOverCell(row: Int, column: Int, val grid: PolarWeaveMaze) : PolarCell(row, column) {

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

        // TODO refactor, almost same in super
        override fun drawSpaceBetweenWalls(g: Graphics2D, inset: Double) {
            if (row == 0) {
                return
            }
            if (grid.isUndercell(this)) {
                return
            }
            g.color = Color.WHITE
            g.stroke = BasicStroke(inset.toFloat())
            drawArc(g, c.r4, Math.toDegrees(c.thetaCw), Math.toDegrees(0.0001))
            drawArc(g, c.r4, Math.toDegrees(c.thetaCcw), Math.toDegrees(0.0001))
            drawArc(g, c.r1, Math.toDegrees(c.thetaCw), Math.toDegrees(0.0001))
            drawArc(g, c.r1, Math.toDegrees(c.thetaCcw), Math.toDegrees(0.0001))

            if (!isLinked(inward)) {
                drawArc(g, c.r1, Math.toDegrees(c.thetaCw), Math.toDegrees(c.theta))
            }
            if (outward.isEmpty()) {
                drawArc(g, c.r4, Math.toDegrees(c.thetaCw), Math.toDegrees(c.theta))
            }
            if (!isLinked(cw)) {
                g.draw(Line2D.Double(c.c0, c.d0))
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

        override fun drawSpaceBetweenWalls(g: Graphics2D, inset: Double) {
            // draw nothing, undercells have no walls
        }

        override fun drawWalls(g: Graphics2D, colorization: Colorization, strokes: Strokes) {
            g.color = colorization.valueFor(this)
            val c = overCell.c
            if (isVerticalPassage()) {
                // TODO das müssten wieder arcs sein
                g.stroke = strokes.getBasicWall()
                g.draw(Line2D.Double(c.a, c.aL))
                g.draw(Line2D.Double(c.b, c.bL))
                g.draw(Line2D.Double(c.c, c.cR))
                g.draw(Line2D.Double(c.d, c.dR))

                g.stroke = strokes.getHiddenWall()
                g.draw(Line2D.Double(c.a, c.c))
                g.draw(Line2D.Double(c.b, c.d))
            } else {
                g.stroke = strokes.getBasicWall()
                g.draw(Line2D.Double(c.a, c.aI))
                g.draw(Line2D.Double(c.b, c.bO))
                g.draw(Line2D.Double(c.c, c.cI))
                g.draw(Line2D.Double(c.d, c.dO))

                g.stroke = strokes.getHiddenWall()
                g.draw(Line2D.Double(c.a, c.b))
                g.draw(Line2D.Double(c.c, c.d))
            }
        }
    }
}
