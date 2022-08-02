package io.github.ocirne.mazes.demos

import io.github.ocirne.mazes.algorithms.Sidewinder
import io.github.ocirne.mazes.grids.cartesian.CartesianGrid
import io.github.ocirne.mazes.grids.cartesian.createAsciiGrid


fun main() {
    val grid = CartesianGrid(4, 4)
    val maze = Sidewinder().on(grid)
    println(maze.createAsciiGrid())
}
