package io.github.ocirne.mazes.demos

import io.github.ocirne.mazes.algorithms.Sidewinder
import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.grids.CartesianGrid
import io.github.ocirne.mazes.output.saveImage


fun main() {
    val grid = CartesianGrid(11, 11)
    Sidewinder().on(grid)
    saveImage(grid.toImage(), "cartesian_sidewinder")

    val colorization = Colorization(grid).dijkstra(grid[10, 5]!!)
    saveImage(grid.toImage(backgroundColors = colorization), "cartesian_sidewinder_colorized")
}
