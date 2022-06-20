package io.github.ocirne.mazes.demos

import io.github.ocirne.mazes.algorithms.Braider
import io.github.ocirne.mazes.grids.CartesianGrid
import io.github.ocirne.mazes.algorithms.RecursiveBacktracker
import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.output.saveImage

fun main() {
    val grid = CartesianGrid(21, 21)
    RecursiveBacktracker.on(grid)
    saveImage(grid.toImage(), "cartesian_recursive_backtracker_dead_ends")

    val colorization1 = Colorization(grid).countLinks()
    saveImage(grid.toImage(colorization = colorization1), "cartesian_recursive_backtracker_dead_ends_colored")

    Braider().braid(grid, p=0.5)
    saveImage(grid.toImage(), "cartesian_recursive_backtracker_dead_ends_braided")

    val colorization2 = Colorization(grid).countLinks()
    saveImage(grid.toImage(colorization = colorization2), "cartesian_recursive_backtracker_dead_ends_braided_colored")
}
