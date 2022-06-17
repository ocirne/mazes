package io.github.ocirne.mazes.colorization

import io.github.ocirne.mazes.grids.Cell
import io.github.ocirne.mazes.grids.Grid
import java.awt.Color
import kotlin.math.roundToInt

class CountLinks(grid: Grid<out Cell>) : Colorization {

    private val weights = grid.eachCell().associateWith { it.links().size }

    private fun max(): Map.Entry<Cell, Int> {
        return weights.entries.maxBy { it.value }
    }

    override fun colorForBackground(cell: Cell): Color? {
        val distance = weights[cell] ?: return null
        val maximum = max().value
        val intensity = distance.toFloat() / maximum
        val dark = 0
        val bright = (255 * intensity).roundToInt()
        return Color(dark, bright, dark)
    }

    override fun colorForWall(cell: Cell): Color {
        return Color.WHITE
    }
}