package io.github.ocirne.mazes.colorization

import io.github.ocirne.mazes.grids.Cell
import io.github.ocirne.mazes.grids.Grid
import java.awt.Color
import kotlin.math.roundToInt

class Distances(val root: Cell) : Colorization {

    val cells: MutableMap<Cell, Int> = mutableMapOf(root to 0)

    operator fun get(cell: Cell): Int? {
        return cells[cell]
    }

    operator fun set(cell: Cell, distance: Int) {
        cells[cell] = distance
    }

    fun cells(): MutableSet<Cell> {
        return cells.keys
    }

    fun pathTo(goal: Cell): Distances {
        var current = goal

        val breadcrumbs = Distances(root)
        breadcrumbs[current] = cells[current]!!

        while (current != root) {
            for (neighbor in current.links()) {
                if (cells[neighbor]!! < cells[current]!!) {
                    breadcrumbs[neighbor] = cells[neighbor]!!
                    current = neighbor
                    break
                }
            }
        }
        return breadcrumbs
    }

    fun max(): MutableMap.MutableEntry<Cell, Int>? {
        return cells.entries.maxByOrNull { it.value }
    }

    /** dijkstra */
    fun distances(start: Cell): Distances {
        val distances = Distances(start)
        var frontier = mutableListOf(start)

        while (frontier.isNotEmpty()) {
            val newFrontier: MutableList<Cell> = mutableListOf()
            for (cell in frontier) {
                for (linked in cell.links()) {
                    if (distances[linked] != null) {
                        continue
                    }
                    distances[linked] = distances[cell]!! + 1
                    newFrontier.add(linked)
                }
            }
            frontier = newFrontier
        }
        return distances
    }
    override fun colorForBackground(cell: Cell): Color? {
        if (this[cell] == null) {
            return null
        }
        val distance = this[cell] ?: return null
        val maximum = this.max()!!.value
        val intensity = (maximum - distance).toFloat() / maximum
        val dark = (255 * intensity).roundToInt()
        val bright = 128 + (127 * intensity).roundToInt()
        return Color(dark, bright, dark)
    }

    override fun colorForWall(cell: Cell): Color {
        return Color.WHITE
    }

    companion object {
        fun <C : Cell> doing(grid: Grid<C>): Distances {
            val start = grid[0, 0]!!
            val distancesObj = Distances(start)
            val distances = distancesObj.distances(start)
            return distances
        }
    }

}