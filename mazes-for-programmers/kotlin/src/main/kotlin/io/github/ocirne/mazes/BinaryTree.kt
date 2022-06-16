package io.github.ocirne.mazes

class BinaryTree {

    companion object {
        fun on(grid: Grid): Grid {
            for (cell in grid.eachCell()) {
                val neighbors = listOfNotNull(cell.north, cell.east)
                if (neighbors.isEmpty()) {
                    continue
                }
                val neighbor = neighbors.random()
                cell.link(neighbor)
            }
            return grid
        }
    }
}