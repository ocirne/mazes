package io.github.ocirne.mazes.grids.cartesian

import io.github.ocirne.mazes.grids.Maze

class CartesianAscii(val maze: CartesianGrid.CartesianMaze) {
    override fun toString(): String {
        return buildString {
            append("+", "---+".repeat(maze.getColumns()), "\n")
            for (row in maze.eachRow()) {
                val top = buildString {
                    append("|")
                    row.forEach { append(contentsOf(it), if (it.isLinked(it.east)) " " else "|") }
                }
                val bottom = buildString {
                    append("+")
                    row.forEach { append(if (it.isLinked(it.south)) "   " else "---", "+") }
                }
                append("$top\n$bottom\n")
            }
        }
    }

    private fun contentsOf(cell: CartesianGrid.CartesianCell): String {
        return "   "
    }
}


fun Maze.createAsciiGrid(): CartesianAscii {
    return CartesianAscii(this as CartesianGrid.CartesianMaze)
}

