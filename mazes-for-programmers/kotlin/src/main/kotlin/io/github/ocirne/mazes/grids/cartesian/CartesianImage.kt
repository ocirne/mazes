package io.github.ocirne.mazes.grids.cartesian

import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.colorization.DefaultBackground
import io.github.ocirne.mazes.colorization.DefaultWalls
import io.github.ocirne.mazes.colorization.Strokes
import io.github.ocirne.mazes.grids.cartesian.CartesianGrid.CartesianCell
import io.github.ocirne.mazes.grids.Maze
import io.github.ocirne.mazes.output.createImage
import io.github.ocirne.mazes.output.save
import java.awt.Graphics2D
import java.awt.geom.Line2D
import java.awt.geom.Rectangle2D
import java.awt.image.BufferedImage

class CartesianImage(val maze: CartesianGrid.CartesianMaze) {

    private val correctionFactor = 1.0

    val baseSize = 10.0
    val wallInset = 0.0
    val backInset = 0.0

    val cellSize = correctionFactor * baseSize
    val imgWidth = cellSize * maze.getColumns()
    val imgHeight = cellSize * maze.getRows()
    val wallInsetAbsolute = cellSize * wallInset
    val backInsetAbsolute = cellSize * backInset

    var drawDeadCells: Boolean = true
    var debug: Boolean = false

    var backgroundColors: Colorization = DefaultBackground(maze)
    var wallColors: Colorization = DefaultWalls(maze)
    var path: Colorization = Colorization(maze)
    var marker: Colorization = Colorization(maze)
    var strokes: Strokes = Strokes(baseSize.toFloat())

    fun render(): BufferedImage {
        val (image, g) = createImage(imgWidth + 1, imgHeight + 1)
        for (mode in Maze.MODES.values()) {
            for (cell in maze.eachCell()) {
                if (!drawDeadCells && cell.links().isEmpty())
                    continue
                when (mode) {
                    Maze.MODES.BACKGROUNDS -> {
                        prepareCoordinates(cell, cellSize, backInsetAbsolute)
                        drawBackground(g, cell, backgroundColors)
                    }
                    Maze.MODES.WALLS -> {
                        prepareCoordinates(cell, cellSize, wallInsetAbsolute)
                        drawWalls(g, cell, wallColors, strokes)
                    }
                    else -> { /* not implemented */ }
                }
            }
        }
        return image
    }

    fun withBackgroundColors(background: Colorization): CartesianImage {
        this.backgroundColors = background
        return this
    }
}

fun Maze.createImage(): CartesianImage {
    return CartesianImage(this as CartesianGrid.CartesianMaze)
}

fun CartesianImage.save(filename: String) {
    this.render().save(filename)
}

data class Coordinates(
    val withInset: Boolean,
    val x1: Double,
    val x2: Double,
    val x3: Double,
    val x4: Double,
    val y1: Double,
    val y2: Double,
    val y3: Double,
    val y4: Double
)

lateinit var c: Coordinates

fun prepareCoordinates(cell: CartesianCell, cellSize: Double, inset: Double) {
    val x1 = cell.column * cellSize
    val x4 = x1 + cellSize
    val x2 = x1 + inset
    val x3 = x4 - inset

    val y1 = cell.row * cellSize
    val y4 = y1 + cellSize
    val y2 = y1 + inset
    val y3 = y4 - inset

    c = Coordinates(inset > 0, x1, x2, x3, x4, y1, y2, y3, y4)
}

fun drawBackground(g: Graphics2D, cell: CartesianCell, colorization: Colorization) {
    g.color = colorization.valueFor(cell)
    if (c.withInset) {
        if (cell !is WeaveGrid.UnderCell) {
            g.fill(Rectangle2D.Double(c.x2, c.y2, c.x3 - c.x2, c.y3 - c.y2))
        }
        if (cell.isLinked(cell.north) && colorization.isValuedCell(cell.north))
            g.fill(Rectangle2D.Double(c.x2, c.y1, c.x3- c.x2, c.y2- c.y1))
        if (cell.isLinked(cell.south) && colorization.isValuedCell(cell.south))
            g.fill(Rectangle2D.Double(c.x2, c.y3, c.x3- c.x2, c.y4- c.y3))
        if (cell.isLinked(cell.west) && colorization.isValuedCell(cell.west))
            g.fill(Rectangle2D.Double(c.x1, c.y2, c.x2- c.x1, c.y3- c.y2))
        if (cell.isLinked(cell.east) && colorization.isValuedCell(cell.east))
            g.fill(Rectangle2D.Double(c.x3, c.y2, c.x4- c.x3, c.y3- c.y2))
    } else {
        if (cell !is WeaveGrid.UnderCell) {
            g.fill(Rectangle2D.Double(c.x1, c.y1, c.x4 - c.x1, c.y4 - c.y1))
        }
    }
}

fun drawWalls(g: Graphics2D, cell: CartesianCell, colorization: Colorization, strokes: Strokes) {
    g.color = colorization.valueFor(cell)
    g.stroke = strokes.getBasicWall()
    if (c.withInset) {
        if (cell.isLinked(cell.north)) {
            g.draw(Line2D.Double(c.x2, c.y1, c.x2, c.y2))
            g.draw(Line2D.Double(c.x3, c.y1, c.x3, c.y2))
        } else {
            g.draw(Line2D.Double(c.x2, c.y2, c.x3, c.y2))
        }
        if (cell.isLinked(cell.south)) {
            g.draw(Line2D.Double(c.x2, c.y3, c.x2, c.y4))
            g.draw(Line2D.Double(c.x3, c.y3, c.x3, c.y4))
        } else {
            g.draw(Line2D.Double(c.x2, c.y3, c.x3, c.y3))
        }
        if (cell.isLinked(cell.west)) {
            g.draw(Line2D.Double(c.x1, c.y2, c.x2, c.y2))
            g.draw(Line2D.Double(c.x1, c.y3, c.x2, c.y3))
        } else {
            g.draw(Line2D.Double(c.x2, c.y2, c.x2, c.y3))
        }
        if (cell.isLinked(cell.east)) {
            g.draw(Line2D.Double(c.x3, c.y2, c.x4, c.y2))
            g.draw(Line2D.Double(c.x3, c.y3, c.x4, c.y3))
        } else {
            g.draw(Line2D.Double(c.x3, c.y2, c.x3, c.y3))
        }
    } else {
        if (cell.north == null)
            g.draw(Line2D.Double(c.x1, c.y1, c.x4, c.y1))
        if (cell.west == null)
            g.draw(Line2D.Double(c.x1, c.y1, c.x1, c.y4))
        if (!cell.isLinked(cell.east))
            g.draw(Line2D.Double(c.x4, c.y1, c.x4, c.y4))
        if (!cell.isLinked(cell.south))
            g.draw(Line2D.Double(c.x1, c.y4, c.x4, c.y4))
    }
}