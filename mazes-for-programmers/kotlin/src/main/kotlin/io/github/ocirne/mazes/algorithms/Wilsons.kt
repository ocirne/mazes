package io.github.ocirne.mazes.algorithms

import io.github.ocirne.mazes.grids.Cell
import io.github.ocirne.mazes.grids.Grid

class Wilsons {

    companion object {
        fun on(grid: Grid<out Cell>) {
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
                        // TODO bei nur position entstehen abgekapselte Bereiche, aber diese werden von dijkstra erreicht
                        path = path.subList(0, position + 1)
                    else
                        path.add(cell)
                }
                for (index in 0..path.size - 2) {
                    path[index].link(path[index + 1])
                    unvisited.remove(path[index])
                }
            }
        }
    }
}
