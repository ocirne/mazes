package io.github.ocirne.mazes.demos

import io.github.ocirne.mazes.colorization.DijkstraDistances
import io.github.ocirne.mazes.grids.CartesianGrid
import io.github.ocirne.mazes.algorithms.RecursiveBacktracker
import io.github.ocirne.mazes.output.saveImage

fun main() {
    val grid = CartesianGrid(20, 20)
    saveImage(grid.toImage(), "cartesian")

    RecursiveBacktracker.on(grid)
    saveImage(grid.toImage(), "cartesian_recursive_backtracker")

    val colorization = DijkstraDistances(grid)
    saveImage(grid.toImage(colorization = colorization), "cartesian_recursive_backtracker_colorized")
}