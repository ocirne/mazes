package io.github.ocirne.mazes

class RecursiveBacktracker {

    companion object {
        fun on(grid: Grid, startAt: Cell = grid.randomCell()): Grid {
            val stack = ArrayDeque<Cell>()
            stack.addLast(startAt)
            while (stack.isNotEmpty()) {
                val current = stack.last()
                val neighbors = current.neighbors().filter { it!!.links.isEmpty() }

                if (neighbors.isEmpty()) {
                    stack.removeLast()
                } else {
                    val neighbor = neighbors.random()
                    current.link(neighbor!!)
                    stack.addLast(neighbor)
                }

            }
            return grid
        }
    }

    fun recursivelyOn(grid: Grid, start_at: Cell = grid.randomCell()): Grid {
        walkFrom(start_at)
        return grid
    }

    fun walkFrom(cell: Cell) {
        for (neighbor in cell.neighbors().shuffled()) {
            if (neighbor!!.links.isEmpty()) {
                cell.link(neighbor)
                walkFrom(neighbor)
            }
        }
    }
}
