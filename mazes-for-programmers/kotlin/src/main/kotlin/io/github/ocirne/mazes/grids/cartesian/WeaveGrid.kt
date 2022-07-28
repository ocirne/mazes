package io.github.ocirne.mazes.grids.cartesian

import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.colorization.Strokes
import io.github.ocirne.mazes.grids.Cell
import java.awt.Graphics2D
import java.awt.geom.Line2D

class WeaveGrid(private val rows: Int, private val columns: Int): CartesianGrid(rows, columns) {

    class WeaveMaze(private val rows: Int, private val columns: Int) : CartesianMaze(rows, columns) {

        private val underCells: MutableList<CartesianCell> = mutableListOf()

        init {
            grid = prepareGrid2()
            configureCells()
        }

        private fun prepareGrid2(): Array<Array<CartesianCell>> {
            return Array(rows) { row -> Array(columns) { column -> OverCell(row, column, this) } }
        }

        fun tunnelUnder(overCell: OverCell) {
            val underCell = UnderCell(overCell)
            underCells.add(underCell)
        }

        override fun eachCell(): List<CartesianCell> {
            if (underCells == null) {
                return super.eachCell()
            }
            return super.eachCell() + underCells
        }

        override fun size(): Int {
            return super.size() + underCells.size
        }
    }

    class OverCell(row: Int, column: Int, val grid: WeaveMaze) : CartesianCell(row, column) {

        override fun neighbors(): List<CartesianCell> {
            val result = super.neighbors().toMutableList()
            if (canTunnelNorth())
                result.add(north!!.north!!)
            if (canTunnelSouth())
                result.add(south!!.south!!)
            if (canTunnelEast())
                result.add(east!!.east!!)
            if (canTunnelWest())
                result.add(west!!.west!!)
            return result.toList()
        }

        private fun canTunnelNorth(): Boolean {
            return north != null && north!!.north != null && isHorizontalPassage(north!!)
        }

        private fun canTunnelSouth(): Boolean {
            return south != null && south!!.south != null && isHorizontalPassage(south!!)
        }

        private fun canTunnelEast(): Boolean {
            return east != null && east!!.east != null && isVerticalPassage(east!!)
        }

        private fun canTunnelWest(): Boolean {
            return west != null && west!!.west != null && isVerticalPassage(west!!)
        }

        fun isHorizontalPassage(cell: CartesianCell): Boolean {
            return cell.isLinked(cell.east) && cell.isLinked(cell.west) && !cell.isLinked(cell.north) && !cell.isLinked(cell.south)
        }

        private fun isVerticalPassage(cell: CartesianCell): Boolean {
            return cell.isLinked(cell.north) && cell.isLinked(cell.south) && !cell.isLinked(cell.east) && !cell.isLinked(cell.west)
        }

        override fun link(cell: Cell, bidi: Boolean) {
            cell as CartesianCell
            val neighbor = if (north != null && north == cell.south) {
                north
            } else if (south != null && south == cell.north) {
                south
            } else if (east != null && east == cell.west) {
                east
            } else if (west != null && west == cell.east) {
                west
            } else {
                null
            }
            if (neighbor != null) {
                grid.tunnelUnder(neighbor as OverCell)
            } else {
                super.link(cell, bidi)
            }
        }
    }

    class UnderCell(overCell: OverCell) : CartesianCell(overCell.row, overCell.column) {
        init {
            if (overCell.isHorizontalPassage(overCell)) {
                north = overCell.north
                overCell.north!!.south = this
                south = overCell.south
                overCell.south!!.north = this

                link(north!!)
                link(south!!)
            } else {
                east = overCell.east
                overCell.east!!.west = this
                west = overCell.west
                overCell.west!!.east = this

                link(east!!)
                link(west!!)
            }
        }

        fun horizontalPassage(): Boolean {
            return east != null || west != null
        }

        private fun verticalPassage(): Boolean {
            return north != null || south != null
        }

        override fun drawWalls(g: Graphics2D, colorization: Colorization, strokes: Strokes) {
            g.color = colorization.valueFor(this)
            if (verticalPassage()) {
                g.stroke = strokes.getBasicWall()
                g.draw(Line2D.Double(c.x2, c.y1, c.x2, c.y2))
                g.draw(Line2D.Double(c.x3, c.y1, c.x3, c.y2))
                g.draw(Line2D.Double(c.x2, c.y3, c.x2, c.y4))
                g.draw(Line2D.Double(c.x3, c.y3, c.x3, c.y4))

                g.stroke = strokes.getHiddenWall()
                g.draw(Line2D.Double(c.x2, c.y2, c.x2, c.y3))
                g.draw(Line2D.Double(c.x3, c.y2, c.x3, c.y3))
            } else {
                g.stroke = strokes.getBasicWall()
                g.draw(Line2D.Double(c.x1, c.y2, c.x2, c.y2))
                g.draw(Line2D.Double(c.x1, c.y3, c.x2, c.y3))
                g.draw(Line2D.Double(c.x3, c.y2, c.x4, c.y2))
                g.draw(Line2D.Double(c.x3, c.y3, c.x4, c.y3))

                g.stroke = strokes.getHiddenWall()
                g.draw(Line2D.Double(c.x2, c.y2, c.x3, c.y2))
                g.draw(Line2D.Double(c.x2, c.y3, c.x3, c.y3))
            }
        }
    }
}
