package io.github.ocirne.mazes.demos

import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.grids.CartesianGrid
import io.github.ocirne.mazes.algorithms.Wilsons
import io.github.ocirne.mazes.output.saveImage

fun main() {
    val grid = CartesianGrid(21, 21)

    Wilsons.on(grid)
    saveImage(grid.toImage(), "cartesian_wilsons")

    val colorization1 = Colorization(grid).dijkstra(grid[20, 10]!!)
    saveImage(grid.toImage(backgroundColors = colorization1), "cartesian_wilsons_colorized1")

    val colorization2 = Colorization(grid).countLinks()
    saveImage(grid.toImage(backgroundColors = colorization2), "cartesian_wilsons_colorized2")
}
