package io.github.ocirne.mazes.demos

import io.github.ocirne.mazes.colorization.DijkstraDistances
import io.github.ocirne.mazes.grids.CartesianGrid
import io.github.ocirne.mazes.algorithms.Wilsons
import io.github.ocirne.mazes.colorization.CountLinks
import io.github.ocirne.mazes.output.saveImage

fun main() {
    val grid = CartesianGrid(21, 21)

    Wilsons.on(grid)
    saveImage(grid.toImage(), "cartesian_wilsons")

    val colorization1 = DijkstraDistances(grid, startAt=grid[20, 10]!!)
    saveImage(grid.toImage(colorization = colorization1), "cartesian_wilsons_colorized1")

    val colorization2 = CountLinks(grid)
    saveImage(grid.toImage(colorization = colorization2), "cartesian_wilsons_colorized2")
}
