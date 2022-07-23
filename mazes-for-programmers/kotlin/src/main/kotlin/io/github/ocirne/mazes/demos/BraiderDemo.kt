package io.github.ocirne.mazes.demos

import io.github.ocirne.mazes.algorithms.Braider
import io.github.ocirne.mazes.algorithms.RecursiveBacktracker
import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.grids.CartesianGrid
import io.github.ocirne.mazes.output.saveImage

fun main() {
    val grid = CartesianGrid(11, 11)
    val maze = RecursiveBacktracker().on(grid)
    saveImage(maze.toImage(), "cartesian_recursive_backtracker_dead_ends")

    val colorization1 = Colorization(maze).countLinks()
    saveImage(maze.toImage(backgroundColors = colorization1), "cartesian_recursive_backtracker_dead_ends_colored")

    Braider().braid(maze, p=0.5)
    saveImage(maze.toImage(), "cartesian_recursive_backtracker_dead_ends_braided")

    val colorization2 = Colorization(maze).countLinks()
    saveImage(maze.toImage(backgroundColors = colorization2), "cartesian_recursive_backtracker_dead_ends_braided_colored")
}
