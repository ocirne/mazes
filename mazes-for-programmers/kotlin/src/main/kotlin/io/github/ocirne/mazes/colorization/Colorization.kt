package io.github.ocirne.mazes.colorization

import io.github.ocirne.mazes.grids.Cell
import io.github.ocirne.mazes.grids.Grid
import java.awt.Color
import kotlin.math.roundToInt

open class Colorization(
    private val grid: Grid,
    private val fromColor: Color = Color.DARK_GRAY,
    private val toColor: Color = Color.GREEN
){

    private val defaultColor = Color.MAGENTA

    private val weights: MutableMap<Cell, Int> = mutableMapOf()

    private val texts: MutableMap<Cell, String> = mutableMapOf()

    var start: Cell? = null

    var goal: Cell? = null

    operator fun set(cell: Cell, value: String) {
        texts[cell] = value
    }

    fun max(): Map.Entry<Cell, Int> {
        return weights.entries.maxBy { it.value }
    }

    fun dijkstra(startAt: Cell = grid.randomCell()): Colorization {
        weights[startAt] = 0
        var frontier = mutableListOf(startAt)

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
        return this
    }

    fun longestPath(): Colorization {
        val bestStart = Colorization(grid).dijkstra().max().key
        start = bestStart
        val distances = Colorization(grid).dijkstra(bestStart)
        var current = distances.max().key
        goal = current
        weights[current] = distances.weights[current]!!
        while (current != start) {
            for (parent in current.links()) {
                if (distances.weights[parent]!! < distances.weights[current]!!) {
                    weights[parent] = distances.weights[parent]!!
                    current = parent
                    break
                }
            }
        }
        return this
    }

    fun countLinks(): Colorization {
        grid.eachCell().forEach { weights[it] = it.links().size }
        return this
    }

    fun isValuedCell(cell: Cell?): Boolean {
        return weights.contains(cell)
    }

    open fun valueFor(cell: Cell): Color {
        val distance = weights[cell] ?: return defaultColor
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

    fun marker(cell: Cell): String? {
        return texts[cell]
    }
}