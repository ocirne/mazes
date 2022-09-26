package io.github.ocirne.mazes.demos

import io.github.ocirne.mazes.algorithms.Wilsons
import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.grids.cartesian.CartesianGrid
import io.github.ocirne.mazes.grids.cartesian.createImage
import io.github.ocirne.mazes.grids.cartesian.saveAs

fun main() {
    val grid = CartesianGrid(11, 11)
    val maze = Wilsons().on(grid)
    maze.createImage().saveAs("cartesian_wilsons")

    val background = Colorization(maze).dijkstra(maze[5, 5]!!)
    maze.createImage()
        .withBackgroundColors(background)
        .saveAs("cartesian_wilsons_colored")
}
