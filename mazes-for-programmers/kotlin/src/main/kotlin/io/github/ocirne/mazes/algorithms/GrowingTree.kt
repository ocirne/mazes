package io.github.ocirne.mazes.algorithms

import io.github.ocirne.mazes.grids.Cell
import io.github.ocirne.mazes.grids.MutableGrid

class GrowingTree(private val f : (List<Cell>) -> Cell):  PassageCarver {

   override fun on(grid: MutableGrid, startAt: Cell) {
        val active = mutableListOf(startAt)
        while (active.isNotEmpty()) {
            val cell: Cell = f.invoke(active)
            val availableNeighbors = cell.neighbors().filter { it.links().isEmpty() }
            if (availableNeighbors.isNotEmpty()) {
                val neighbor = availableNeighbors.random()
                cell.link(neighbor)
                active.add(neighbor)
            } else {
                active.remove(cell)
            }
        }
    }
}
