package io.github.ocirne.mazes.algorithms

import io.github.ocirne.mazes.grids.Cell
import io.github.ocirne.mazes.grids.GridProvider
import io.github.ocirne.mazes.grids.MutableGrid

class Wilsons : PassageCarver {

    override fun on(gridProvider: GridProvider, startAt: (MutableGrid) -> Cell): MutableGrid {
        val grid = gridProvider.createPassageCarver()
        val unvisited = grid.eachCell().toMutableList()
        val first = unvisited.random()
        unvisited.remove(first)
        while (unvisited.isNotEmpty()) {
            var cell = unvisited.random()
            var path = mutableListOf(cell)
            while (unvisited.contains(cell)) {
                cell = cell.neighbors().random()
                val position = path.indexOf(cell)
                if (position >= 0)
                    path = path.subList(0, position + 1)
                else
                    path.add(cell)
            }
            path.zipWithNext { v, w -> v.link(w) }
            unvisited.removeAll(path.subList(0, path.size-1).toSet())
        }
        return grid
    }
}
