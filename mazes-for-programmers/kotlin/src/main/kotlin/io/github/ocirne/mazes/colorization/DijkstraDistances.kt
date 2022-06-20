package io.github.ocirne.mazes.colorization

import io.github.ocirne.mazes.grids.Cell
import io.github.ocirne.mazes.grids.Grid
import java.awt.Color
import kotlin.math.roundToInt

class DijkstraDistances(
    private val grid: Grid,
    startAt: Cell = grid.randomCell(noNeighborsAllowed=false),
    private val fromColor: Color = Color.DARK_GRAY,
    private val toColor: Color = Color.GREEN
) : Colorization {

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
                    if (weights.contains(linked)) {
                        continue
                    }
                    weights[linked] = weights[cell]!! + 1
                    newFrontier.add(linked)
                }
            }
            frontier = newFrontier
        }
    }

    override fun colorForBackground(cell: Cell): Color {
        val distance = weights[cell] ?: return Color.BLACK
        val maximum = max().value
        val intensity = (maximum - distance).toFloat() / maximum
        val rangeRed = toColor.red - fromColor.red
        val rangeGreen = toColor.green - fromColor.green
        val rangeBlue = toColor.blue - fromColor.blue
        val r = (fromColor.red + rangeRed * intensity).roundToInt()
        val g = (fromColor.green + rangeGreen * intensity).roundToInt()
        val b = (fromColor.blue + rangeBlue * intensity).roundToInt()
        return Color(r, g, b)
    }

    override fun colorForWall(cell: Cell): Color {
        return Color.WHITE
    }
}