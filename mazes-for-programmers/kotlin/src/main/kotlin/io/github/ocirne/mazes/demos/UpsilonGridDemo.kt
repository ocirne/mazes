package io.github.ocirne.mazes.demos

import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.algorithms.RecursiveBacktracker
import io.github.ocirne.mazes.grids.UpsilonGrid
import io.github.ocirne.mazes.output.saveImage

fun main() {
    val grid = UpsilonGrid(11, 11)
    val maze = RecursiveBacktracker().on(grid)
    saveImage(maze.toImage(), "upsilon_recursive_backtracker")

    val colorization1 = Colorization(maze).dijkstra(maze[10, 5]!!)
    saveImage(maze.toImage(backgroundColors = colorization1), "upsilon_recursive_backtracker_colorized1")

    val colorization2 = Colorization(maze).countLinks()
    saveImage(maze.toImage(backgroundColors = colorization2), "upsilon_recursive_backtracker_colorized2")
}
