package io.github.ocirne.mazes.demos

import io.github.ocirne.mazes.algorithms.Ellers
import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.grids.cartesian.CartesianGrid
import io.github.ocirne.mazes.output.save


fun main() {
    val grid = CartesianGrid(11, 11)
    val maze = Ellers().onCartesianGrid(grid)
    maze.toImage().save("cartesian_ellers")

    val colorization = Colorization(maze).dijkstra(maze[5, 5]!!)
    maze.toImage(backgroundColors = colorization).save("cartesian_ellers_colored")
}
