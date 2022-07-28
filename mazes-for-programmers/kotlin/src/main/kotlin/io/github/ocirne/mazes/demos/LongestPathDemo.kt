package io.github.ocirne.mazes.demos

import io.github.ocirne.mazes.algorithms.Sidewinder
import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.grids.cartesian.CartesianGrid
import io.github.ocirne.mazes.output.saveImage

fun main() {
    val grid = CartesianGrid(11, 11)
    val maze = Sidewinder().on(grid)

    val longestPath = Colorization(maze).longestPath()
    saveImage(maze.toImage(wallInset=0.1, path = longestPath), "cartesian_recursive_backtracker_longest_path")
}
