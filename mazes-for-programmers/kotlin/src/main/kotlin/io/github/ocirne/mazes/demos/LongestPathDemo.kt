package io.github.ocirne.mazes.demos

import io.github.ocirne.mazes.algorithms.Sidewinder
import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.grids.CartesianGrid
import io.github.ocirne.mazes.output.saveImage

fun main() {
    val grid = CartesianGrid(21, 21)
    Sidewinder().on(grid)

    val longestPath = Colorization(grid).longestPath()
    saveImage(grid.toImage(colorization = longestPath), "cartesian_recursive_backtracker_longest_path")
}
