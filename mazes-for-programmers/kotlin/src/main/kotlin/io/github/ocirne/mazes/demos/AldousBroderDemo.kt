package io.github.ocirne.mazes.demos

import io.github.ocirne.mazes.algorithms.AldousBroder
import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.grids.CartesianGrid
import io.github.ocirne.mazes.output.saveImage


fun main() {
    val grid = CartesianGrid(11, 11)

    val maze = AldousBroder().on(grid)
    saveImage(maze.toImage(), "cartesian_aldous_broder")

    val colorization = Colorization(maze).dijkstra(maze[10, 5]!!)
    saveImage(maze.toImage(backgroundColors = colorization), "cartesian_aldous_broder_colorized")
}
