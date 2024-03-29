package io.github.ocirne.mazes.grids

import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.colorization.DefaultBackground
import io.github.ocirne.mazes.colorization.DefaultWalls
import io.github.ocirne.mazes.colorization.Strokes
import java.awt.image.BufferedImage

interface Maze {

    operator fun get(row: Int, column: Int): Cell?

    fun size(): Int

    fun eachCell(): List<Cell>

    fun eachRow(reversed: Boolean = false): Iterator<Array<out Cell>> {
        throw NotImplementedError()
    }

    fun randomCell(noNeighborsAllowed:Boolean=true): Cell {
        while (true) {
            val result = eachCell().random()
            if (noNeighborsAllowed || result.links().isNotEmpty()) {
                return result
            }
        }
    }

    fun deadEnds(): List<Cell> {
        return eachCell().filter { it.links().size == 1 }
    }

    enum class MODES {
        BACKGROUNDS, FAKE, WALLS, PATH, MARKER
    }

    fun toImage(
        baseSize: Double = 20.0,
        wallInset: Double = 0.0,
        backInset: Double = 0.0,
        drawDeadCells: Boolean = true,
        debug: Boolean = false,
        backgroundColors: Colorization = DefaultBackground(this),
        wallColors: Colorization = DefaultWalls(this),
        path: Colorization = Colorization(this),
        marker: Colorization = Colorization(this),
        strokes: Strokes = Strokes(baseSize.toFloat())
    ): BufferedImage
}
