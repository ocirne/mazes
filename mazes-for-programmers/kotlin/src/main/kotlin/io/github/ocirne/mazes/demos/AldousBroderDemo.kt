package io.github.ocirne.mazes.demos

import io.github.ocirne.mazes.algorithms.AldousBroder
import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.grids.cartesian.CartesianGrid
import io.github.ocirne.mazes.grids.cartesian.createImage
import io.github.ocirne.mazes.grids.cartesian.saveAs

fun main() {
    val grid = CartesianGrid(11, 11)

    val maze = AldousBroder().on(grid)
    maze.createImage().saveAs("cartesian_aldous_broder")

    val colorization = Colorization(maze).dijkstra(maze[5, 5]!!)
    maze.createImage()
        .withBackgroundColors(colorization)
        .saveAs("cartesian_aldous_broder_colored")
}
