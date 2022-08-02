package io.github.ocirne.mazes.demos

import io.github.ocirne.mazes.grids.cartesian.CartesianGrid
import io.github.ocirne.mazes.grids.cartesian.createImage
import io.github.ocirne.mazes.grids.cartesian.save


fun main() {
    val grid = CartesianGrid(4, 4)
    val maze = grid.forPassageCarver()

    maze.createImage().save("cartesian")
}
