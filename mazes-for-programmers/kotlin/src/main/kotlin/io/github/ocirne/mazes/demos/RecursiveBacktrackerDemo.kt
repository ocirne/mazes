package io.github.ocirne.mazes.demos

import io.github.ocirne.mazes.colorization.DijkstraDistances
import io.github.ocirne.mazes.grids.CartesianGrid
import io.github.ocirne.mazes.algorithms.RecursiveBacktracker
import io.github.ocirne.mazes.colorization.CountLinks
import io.github.ocirne.mazes.output.saveImage

fun main() {
    val grid = CartesianGrid(20, 20)
    saveImage(grid.toImage(), "cartesian")

    RecursiveBacktracker.on(grid)
    saveImage(grid.toImage(), "cartesian_recursive_backtracker")

    val colorization1 = DijkstraDistances(grid)
    saveImage(grid.toImage(colorization = colorization1), "cartesian_recursive_backtracker_colorized1")

    val colorization2 = CountLinks(grid)
    saveImage(grid.toImage(colorization = colorization2), "cartesian_recursive_backtracker_colorized2")
}
