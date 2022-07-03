package io.github.ocirne.mazes.demos

import io.github.ocirne.mazes.algorithms.HuntAndKill
import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.grids.CartesianGrid
import io.github.ocirne.mazes.output.saveImage

fun main() {
    val grid = CartesianGrid(11, 11)

    HuntAndKill.on(grid)
    saveImage(grid.toImage(), "cartesian_huntAndKill_backtracker")

    val colorization1 = Colorization(grid).dijkstra(grid[10, 5]!!)
    saveImage(grid.toImage(backgroundColors = colorization1), "cartesian_huntAndKill_colorized1")

    val colorization2 = Colorization(grid).countLinks()
    saveImage(grid.toImage(backgroundColors = colorization2), "cartesian_huntAndKill_colorized2")
}
