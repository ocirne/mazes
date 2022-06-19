package io.github.ocirne.mazes.demos

import io.github.ocirne.mazes.colorization.DijkstraDistances
import io.github.ocirne.mazes.grids.PolarGrid
import io.github.ocirne.mazes.algorithms.RecursiveBacktracker
import io.github.ocirne.mazes.output.saveImage

fun main() {
    val grid = PolarGrid(8)
    saveImage(grid.toImage(), "polar")

    RecursiveBacktracker.on(grid)
    saveImage(grid.toImage(), "polar_recursive_backtracker")

    val colorization = DijkstraDistances(grid, startAt=grid[7, 36]!!)
    saveImage(grid.toImage(colorization = colorization), "polar_recursive_backtracker_colorized")
}
