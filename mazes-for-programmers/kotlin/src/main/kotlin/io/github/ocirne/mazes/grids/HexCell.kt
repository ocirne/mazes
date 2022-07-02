package io.github.ocirne.mazes.grids

import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.colorization.Strokes
import java.awt.Graphics2D
import java.awt.Polygon

class HexCell(val row: Int, val column: Int) : Cell() {

    var north: HexCell? = null
    var northeast: HexCell? = null
    var southeast: HexCell? = null
    var south: HexCell? = null
    var southwest: HexCell? = null
    var northwest: HexCell? = null

    override fun neighbors(): List<HexCell> {
        return arrayListOf(north, northeast, southeast, south, southwest, northwest).filterNotNull()
    }

    data class Coordinates(
        val xFW: Int,
        val xNW: Int,
        val xNE: Int,
        val xFE: Int,

        val yN: Int,
        val yM: Int,
        val yS: Int
    )

    private lateinit var c: Coordinates

    fun prepareCoordinates(cellSize: Int, height: Double, aSize:Double, bSize:Double) {
        val cx = cellSize + 3 * column * aSize
        val cy = bSize + row * height + (column % 2) * bSize

        val xFW = (cx - cellSize).toInt()
        val xNW = (cx - aSize).toInt()
        val xNE = (cx + aSize).toInt()
        val xFE = (cx + cellSize).toInt()

        val yN = (cy - bSize).toInt()
        val yM = cy.toInt()
        val yS = (cy + bSize).toInt()
        c = Coordinates(xFW, xNW, xNE, xFE, yN, yM, yS)
    }

    override fun drawBackground(g: Graphics2D, colorization: Colorization) {
        g.color = colorization.valueFor(this)
        // TODO switch to Path2d (all polygons)
        val p = Polygon()
        p.addPoint(c.xFW, c.yM)
        p.addPoint(c.xNW, c.yN)
        p.addPoint(c.xNE, c.yN)
        p.addPoint(c.xFE, c.yM)
        p.addPoint(c.xNE, c.yS)
        p.addPoint(c.xNW, c.yS)
        g.fillPolygon(p)
    }

    override fun drawWalls(g: Graphics2D, colorization: Colorization, strokes: Strokes) {
        g.color = colorization.valueFor(this)
        g.stroke = strokes.getBasicWall()
        if (southwest == null)
            g.drawLine(c.xFW, c.yM, c.xNW, c.yS)
        if (northwest == null)
            g.drawLine(c.xFW, c.yM, c.xNW, c.yN)
        if (north == null)
            g.drawLine(c.xNW, c.yN, c.xNE, c.yN)
        if (!isLinked(northeast))
            g.drawLine(c.xNE, c.yN, c.xFE, c.yM)
        if (!isLinked(southeast))
            g.drawLine(c.xFE, c.yM, c.xNE, c.yS)
        if (!isLinked(south))
            g.drawLine(c.xNE, c.yS, c.xNW, c.yS)
    }
}
