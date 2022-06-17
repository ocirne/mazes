package io.github.ocirne.mazes.colorization

import io.github.ocirne.mazes.grids.Cell
import io.github.ocirne.mazes.grids.Grid
import java.awt.Color
import kotlin.math.roundToInt

class DijkstraDistances(private val grid: Grid<out Cell>, startAt:Cell=grid.randomCell()) : Colorization {

    private val weights: MutableMap<Cell, Int> = mutableMapOf(startAt to 0)

    init {
        distances(startAt)
    }

    private fun max(): Map.Entry<Cell, Int> {
        return weights.entries.maxBy { it.value }
    }

    /** dijkstra */
    private fun distances(start: Cell) {
        var frontier = mutableListOf(start)

        while (frontier.isNotEmpty()) {
            val newFrontier: MutableList<Cell> = mutableListOf()
            for (cell in frontier) {
                for (linked in cell.links()) {
                    if (weights[linked] != null) {
                        continue
                    }
                    weights[linked] = weights[cell]!! + 1
                    newFrontier.add(linked)
                }
            }
            frontier = newFrontier
        }
    }

    override fun colorForBackground(cell: Cell): Color? {
        val distance = weights[cell] ?: return null
        val maximum = max().value
        val intensity = (maximum - distance).toFloat() / maximum
        val dark = (255 * intensity).roundToInt()
        val bright = (128 + 127 * intensity).roundToInt()
        return Color(dark, bright, dark)
    }

    override fun colorForWall(cell: Cell): Color {
        return Color.WHITE
    }
}