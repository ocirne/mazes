package io.github.ocirne.mazes.demos

import io.github.ocirne.mazes.algorithms.RecursiveBacktracker
import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.grids.cartesian.CartesianGrid
import io.github.ocirne.mazes.grids.cartesian.createImage
import io.github.ocirne.mazes.grids.cartesian.saveAs

fun main() {
    val grid = CartesianGrid(11, 11)
    val maze = RecursiveBacktracker().on(grid)

    maze.createImage().saveAs("cartesian_recursive_backtracker")

    val colorization = Colorization(maze).dijkstra(maze[5, 5]!!)
    maze.createImage()
        .withBackgroundColors(colorization)
        .saveAs("cartesian_recursive_backtracker_colored")
}
