package io.github.ocirne.mazes.grids.polar

import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.colorization.Strokes
import io.github.ocirne.mazes.grids.Cell
import io.github.ocirne.mazes.grids.GridProvider
import io.github.ocirne.mazes.grids.Maze
import io.github.ocirne.mazes.grids.cartesian.CartesianGrid
import io.github.ocirne.mazes.output.createImage
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D
import java.awt.geom.*
import java.awt.image.BufferedImage
import java.lang.Thread.yield
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

open class PolarGrid(private val rows: Int) : GridProvider {

    override fun forPassageCarver(): PolarMaze {
        return PolarMaze(rows)
    }

    override fun forWallAdder(): PolarMaze {
        val grid = PolarMaze(rows)
        for (cell in grid.eachCell()) {
            cell.neighbors().forEach { n -> cell.link(n, false) }
        }
        return grid
    }

    open class PolarMaze(private val rows: Int) : Maze {

        private val correctionFactor = 1.0

        var grid: Array<Array<PolarCell>>

        init {
            grid = prepareGrid()
            configureCells()
        }

        private fun prepareGrid(): Array<Array<PolarCell>> {
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
                rings.add(Array(cells) { col -> PolarCell(row, col) })
            }
            return Array(rows) { row -> rings[row] }
        }

        fun configureCells() {
            for (cell in eachCell()) {
                val row = cell.row
                val col = cell.column
                if (row > 0) {
                    cell.cw = this[row, col + 1]
                    cell.ccw = this[row, col - 1]

                    val ratio = grid[row].size / grid[row - 1].size
                    val parent = grid[row - 1][col / ratio]
                    parent.outward.add(cell)
                    cell.inward = parent
                }
            }
        }

        operator fun get(row: Int): Array<PolarCell>? {
            if (row < 0 || rows <= row) {
                return null
            }
            return grid[row]
        }

        override operator fun get(row: Int, column: Int): PolarCell? {
            if (row < 0 || rows <= row) {
                return null
            }
            return grid[row][Math.floorMod(column, grid[row].size)]
        }

        override fun size(): Int {
            return grid.flatten().size
        }

        override fun eachCell(): List<PolarCell> {
            return grid.flatten()
        }

        override fun eachRow(reversed: Boolean): Iterator<Array<PolarCell>> {
            return if (reversed) grid.reversed().iterator() else grid.iterator()
        }

        private fun debugGrid(g: Graphics2D, cellSize: Double, center: Double, wallInset: Double) {
            val dashed = BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0.0f, floatArrayOf(9.0f), 0.0f)
            g.stroke = dashed
            g.color = Color.WHITE
            val theta = 2 * PI / grid[rows-1].size
            val outerRadius = rows * cellSize
            val radiusInset = wallInset * cellSize
            val innerRadius = cellSize - radiusInset
            val thetaInset = radiusInset * 2* PI / 6
            val thetaInsetInnen = thetaInset / innerRadius
            val thetaInsetAussen = thetaInset / outerRadius

            for (row in 0 .. rows) {
                val radius1 = row * cellSize - radiusInset
                g.draw(Ellipse2D.Double(center - radius1, center - radius1, 2.0 * radius1, 2.0 * radius1))
                val radius2 = row * cellSize + radiusInset
                g.draw(Ellipse2D.Double(center - radius2, center - radius2, 2.0 * radius2, 2.0 * radius2))
            }
            for (t in 0 .. grid[rows-1].size) {
                val x1 = center + innerRadius * cos(t * theta + thetaInsetInnen)
                val y1 = center - innerRadius * sin(t * theta + thetaInsetInnen)
                val x2 = center + innerRadius * cos(t * theta - thetaInsetInnen)
                val y2 = center - innerRadius * sin(t * theta - thetaInsetInnen)

                val x3 = center + outerRadius * cos(t * theta + thetaInsetAussen)
                val y3 = center - outerRadius * sin(t * theta + thetaInsetAussen)
                val x4 = center + outerRadius * cos(t * theta - thetaInsetAussen)
                val y4 = center - outerRadius * sin(t * theta - thetaInsetAussen)

                g.draw(Line2D.Double(x1, y1, x3, y3))
                g.draw(Line2D.Double(x2, y2, x4, y4))
            }
        }

        override fun toImage(
            baseSize: Double,
            wallInset: Double,
            backInset: Double,
            drawDeadCells: Boolean,
            debug: Boolean,
            backgroundColors: Colorization,
            wallColors: Colorization,
            path: Colorization,
            marker: Colorization,
            strokes: Strokes
        ): BufferedImage {
            val cellSize = correctionFactor * baseSize
            val imgSize = 2 * rows * cellSize + 4

            val (image, g) = createImage(imgSize + 1, imgSize + 1)
            val center = imgSize / 2

            if (debug)
                debugGrid(g, cellSize, center, wallInset)

            for (mode in Maze.MODES.values()) {
                for (cell in eachCell()) {
                    when (mode) {
                        Maze.MODES.BACKGROUNDS -> {
                            cell.prepareCoordinates(this, center, cellSize, backInset)
                            cell.drawBackground(g, backgroundColors)
                        }
                        Maze.MODES.FAKE -> {
                            cell.prepareCoordinates(this, center, cellSize, backInset)
                            cell.drawSpaceBetweenWalls(g, 2 * backInset * cellSize)
                        }
                        Maze.MODES.WALLS -> {
                            // TODO vielleicht trennen in coordinaten und insets?
                            cell.prepareCoordinates(this, center, cellSize, wallInset)
                            cell.drawWalls(g, wallColors, strokes)
                        }
                        Maze.MODES.PATH -> {
                            cell.prepareCoordinates(this, center, cellSize)
                            cell.drawPath(g, path, strokes,this, center, cellSize)
                        }
                        Maze.MODES.MARKER -> {
                            cell.prepareCoordinates(this, center, cellSize)
                            cell.drawMarker(g, marker, this, center, cellSize)
                        }
                    }
                }
            }

            if (wallInset == 0.0) {
                val radius = rows * cellSize
                g.color = wallColors.valueFor(grid[0][0])
                val xy = (center - radius).toInt()
                val wh = (2*radius).toInt()
                g.drawOval(xy, xy, wh, wh)
            }
            return image
        }
    }

    open class PolarCell(val row: Int, val column: Int) : Cell() {

        var cw: PolarCell? = null
        var ccw: PolarCell? = null
        var inward: PolarCell? = null
        val outward: MutableList<PolarCell> = mutableListOf()

        data class Coordinates(
            val withInset: Boolean,
            val center: Double,
            val cellSize: Double,
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
            val a0: Point2D,
            val b0: Point2D,
            val c0: Point2D,
            val d0: Point2D,
        )

        lateinit var c: Coordinates

        private fun toPolar(center: Double, radius: Double, angle: Double): Point2D {
            return Point2D.Double(center + radius * cos(angle), center - radius * sin(angle))
        }

        fun prepareCoordinates(grid: PolarMaze, center: Double, cellSize: Double, inset: Double=0.0) {
            val theta = 2 * PI / grid[row]!!.size
            val r1 = row * cellSize
            val r4 = (row + 1) * cellSize
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

            val a0 = toPolar(center, r1, thetaCw)
            val b0 = toPolar(center, r4, thetaCw)
            val c0 = toPolar(center, r1, thetaCcw)
            val d0 = toPolar(center, r4, thetaCcw)

            c = Coordinates(inset > 0, center, cellSize, r1, r2, r3, r4, theta,
                aa, aI, aL, bb, bL, bO, cc, cI, cR, dd, dO, dR, ee, eO, ff, fO,
                thetaCw,
                thetaCcw,
                thetaInset1,
                thetaInset2,
                thetaInset3,
                thetaInset4, a0, b0, c0, d0
            )
        }

        override fun neighbors(): List<PolarCell> {
            return arrayListOf(cw, ccw, inward).filterNotNull() + outward
        }

        protected fun drawArc(g: Graphics2D, r: Double, start: Double, extent: Double) {
            val xy = c.center - r
            val wh = 2 * r
            g.draw(Arc2D.Double(xy, xy, wh, wh, start, extent, Arc2D.OPEN))
        }

        private fun drawTile(g: Graphics2D, r1: Double, r2: Double, start: Double, extent: Double) {
            val xy1 = c.center - r2
            val wh1 = 2.0 * r2
            val outerPie = Area(Arc2D.Double(xy1, xy1, wh1, wh1, Math.toDegrees(start), Math.toDegrees(extent), Arc2D.PIE))
            val xy2 = c.center - r1
            val wh2 = 2.0 * r1
            val innerPie = Area(Arc2D.Double(xy2, xy2, wh2, wh2, Math.toDegrees(start), Math.toDegrees(extent), Arc2D.PIE))
            outerPie.subtract(innerPie)
            g.fill(outerPie)
        }

        override fun drawBackground(g: Graphics2D, colorization: Colorization) {
            g.color = colorization.valueFor(this)
            if (row == 0) {
                g.fill(Ellipse2D.Double(c.center - c.r4, c.center - c.r4, 2.0 * c.r4, 2.0 * c.r4))
            } else {
                drawTile(g, c.r1, c.r4, c.thetaCw, c.theta)
            }
        }

        open fun drawSpaceBetweenWalls(g: Graphics2D, inset: Double) {
            if (row == 0 || inset == 0.0) {
                return
            }
            // TODO Generelle Hintergrundfarbe?
            g.color = Color.WHITE
            g.stroke = BasicStroke(inset.toFloat())
            drawArc(g, c.r1, Math.toDegrees(c.thetaCw), Math.toDegrees(20.0))
            drawArc(g, c.r1, Math.toDegrees(c.thetaCcw), Math.toDegrees(20.0))
            drawArc(g, c.r4, Math.toDegrees(c.thetaCw), Math.toDegrees(20.0))
            drawArc(g, c.r4, Math.toDegrees(c.thetaCcw), Math.toDegrees(20.0))

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
        override fun drawWalls(g: Graphics2D, colorization: Colorization, strokes: Strokes) {
            g.color = colorization.valueFor(this)
            g.stroke = strokes.getBasicWall()
            if (c.withInset) {
                if (row > 0) {
                    if (isLinked(cw)) {
                        drawArc(g, c.r2, Math.toDegrees(c.thetaCcw - c.thetaInset2), Math.toDegrees(c.thetaInset2))
                        drawArc(g, c.r3, Math.toDegrees(c.thetaCcw - c.thetaInset3), Math.toDegrees(c.thetaInset3))
                    } else {
                        g.draw(Line2D.Double(c.a, c.b))
                    }
                    if (isLinked(ccw)) {
                        drawArc(g, c.r2, Math.toDegrees(c.thetaCw), Math.toDegrees(c.thetaInset2))
                        drawArc(g, c.r3, Math.toDegrees(c.thetaCw), Math.toDegrees(c.thetaInset3))
                    } else {
                        g.draw(Line2D.Double(c.c, c.d))
                    }
                    if (isLinked(inward)) {
                        g.draw(Line2D.Double(c.a, c.aI))
                        g.draw(Line2D.Double(c.c, c.cI))
                    } else {
                        drawArc(g, c.r2,
                            Math.toDegrees(c.thetaCw + c.thetaInset2),
                            Math.toDegrees(c.theta - (2 * c.thetaInset2))
                        )
                    }
                }
                if (outward.isEmpty()) {
                    // Outer rim
                    drawArc(g, c.r3,
                        Math.toDegrees(c.thetaCw + c.thetaInset3),
                        Math.toDegrees(c.theta - (2 * c.thetaInset3))
                    )
                } else if (outward.size == 1) {
                    if (isLinked(outward[0])) {
                        g.draw(Line2D.Double(c.b, c.bO))
                        g.draw(Line2D.Double(c.d, c.dO))
                    } else {
                        drawArc(g, c.r3,
                            Math.toDegrees(c.thetaCw + c.thetaInset3),
                            Math.toDegrees(c.theta - (2 * c.thetaInset3))
                        )
                    }
                } else if (outward.size == 2) {
                    if (isLinked(outward[0])) {
                        g.draw(Line2D.Double(c.d, c.dO))
                        g.draw(Line2D.Double(c.f, c.fO))
                    } else {
                        drawArc(g, c.r3, Math.toDegrees(c.thetaCw + c.thetaInset3),
                            Math.toDegrees(0.5 * c.theta - (2 * c.thetaInset3))
                        )
                    }
                    drawArc(g, c.r3, Math.toDegrees(c.thetaCw + 0.5 * c.theta - c.thetaInset3),
                        Math.toDegrees(2 * c.thetaInset3)
                    )
                    if (isLinked(outward[1])) {
                        g.draw(Line2D.Double(c.b, c.bO))
                        g.draw(Line2D.Double(c.e, c.eO))
                    } else {
                        drawArc(g, c.r3, Math.toDegrees(c.thetaCw + 0.5 * c.theta + c.thetaInset3),
                            Math.toDegrees(0.5 * c.theta - (2 * c.thetaInset3))
                        )
                    }
                } else {
                    val subTheta = c.theta / outward.size
                    outward.forEachIndexed { index, outwardCell ->
                        if (isLinked(outwardCell)) {
                            val b = toPolar(c.center, c.r3, c.thetaCw + (index + 1) * subTheta - c.thetaInset3)
                            val bO = toPolar(c.center, c.r4, c.thetaCw + (index + 1) * subTheta - c.thetaInset4)
                            val e = toPolar(c.center, c.r3, c.thetaCw + index * subTheta + c.thetaInset3)
                            val eO = toPolar(c.center, c.r4, c.thetaCw + index * subTheta + c.thetaInset4)
                            g.draw(Line2D.Double(b, bO))
                            g.draw(Line2D.Double(e, eO))
                        } else {
                            drawArc(
                                g, c.r3,
                                Math.toDegrees(c.thetaCw + index * subTheta + c.thetaInset3),
                                Math.toDegrees(subTheta - 2 * c.thetaInset3)
                            )
                        }
                        drawArc(
                            g, c.r3,
                            Math.toDegrees(c.thetaCw + (index + 1) * subTheta - c.thetaInset3),
                            Math.toDegrees(2 * c.thetaInset3)
                        )
                    }
                }
            } else {
                if (!isLinked(inward)) {
                    drawArc(g, c.r1, Math.toDegrees(c.thetaCw), Math.toDegrees(c.theta))
                }
                if (!isLinked(cw))
                    g.draw(Line2D.Double(c.a, c.b))
            }
        }

        private fun middle(grid: PolarMaze, center: Double, cellSize: Double): Point2D {
            val r = (row +0.5) * cellSize
            val theta = 2 * PI / grid[row]!!.size
            val t = (column+0.5) * theta
            return toPolar(center, r, t)
        }

        fun drawPath(g: Graphics2D, colorization: Colorization, strokes: Strokes, grid: PolarMaze, center: Double, cellSize: Double) {
            g.color = colorization.valueFor(this)
            if (g.color == Color.WHITE) {
                return
            }
            val m = middle(grid, center, cellSize)
            for (other in links()) {
                if (!colorization.isValuedCell(other)) {
                    return
                }
                other as PolarCell
                val o = other.middle(grid, center, cellSize)
                g.color = Color.MAGENTA
                g.stroke = strokes.getPath()
                g.draw(Line2D.Double(m, o))
            }
        }

        fun drawMarker(g: Graphics2D, colorization: Colorization, grid: PolarMaze, center: Double, cellSize: Double) {
            val marker = colorization.marker(this) ?: return
            val m = middle(grid, center, cellSize)
            val size = cellSize / 4
            g.color = Color.RED
            g.fillOval((m.x - size).toInt(), (m.y-size).toInt(), (2*size).toInt(), (2*size).toInt())
            g.color = Color.WHITE
            g.font = Font("Sans", Font.BOLD or Font.CENTER_BASELINE, size.toInt())
            g.drawString(marker, (m.x - size / 2).toFloat(), (m.y + size / 3).toFloat())
        }
    }

}