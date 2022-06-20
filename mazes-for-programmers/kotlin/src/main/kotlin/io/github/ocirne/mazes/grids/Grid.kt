package io.github.ocirne.mazes.grids

import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.colorization.NoBackground
import java.awt.image.RenderedImage

interface Grid {

    operator fun get(row: Int, column: Int): Cell?

    fun size(): Int

    fun eachCell(): List<Cell>

    fun randomCell(): Cell {
        return eachCell().random()
    }

    fun deadEnds(): List<Cell> {
        return eachCell().filter { it.links().size == 1 }
    }

    enum class MODES {
        BACKGROUNDS, WALLS
    }

    fun toImage(cellSize: Int = 40, colorization: Colorization = NoBackground()): RenderedImage
}
