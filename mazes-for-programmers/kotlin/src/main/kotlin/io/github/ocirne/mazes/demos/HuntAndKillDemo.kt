package io.github.ocirne.mazes.demos

import io.github.ocirne.mazes.algorithms.HuntAndKill
import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.grids.CartesianGrid
import io.github.ocirne.mazes.output.saveImage

fun main() {
    val grid = CartesianGrid(11, 11)

    val maze = HuntAndKill().on(grid)
    saveImage(maze.toImage(), "cartesian_huntAndKill_backtracker")

    val colorization1 = Colorization(maze).dijkstra(maze[10, 5]!!)
    saveImage(maze.toImage(backgroundColors = colorization1), "cartesian_huntAndKill_colorized1")

    val colorization2 = Colorization(maze).countLinks()
    saveImage(maze.toImage(backgroundColors = colorization2), "cartesian_huntAndKill_colorized2")
}
