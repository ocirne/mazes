package io.github.ocirne.mazes.grids

import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.colorization.DefaultBackground
import io.github.ocirne.mazes.colorization.DefaultWalls
import java.awt.image.RenderedImage

interface Grid {

    operator fun get(row: Int, column: Int): Cell?

    fun size(): Int

    fun eachCell(): List<Cell>

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
        BACKGROUNDS, WALLS, PATH, MARKER
    }

    fun toImage(cellSize: Int = 100, wallInset:Double=0.0, backInset: Double=0.0,
                backgroundColors: Colorization = DefaultBackground(this),
                wallColors: Colorization = DefaultWalls(this),
                path: Colorization = Colorization(this),
                marker: Colorization = Colorization(this)): RenderedImage
}
