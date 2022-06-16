package io.github.ocirne.mazes

class Distances(val root: Cell) {

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
}