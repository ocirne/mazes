package io.github.ocirne.mazes.demos

import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.algorithms.RecursiveBacktracker
import io.github.ocirne.mazes.grids.HexGrid
import io.github.ocirne.mazes.output.saveImage

fun main() {
    val grid = HexGrid(21, 21)
    saveImage(grid.toImage(), "hex")

    RecursiveBacktracker.on(grid)
    saveImage(grid.toImage(), "hex_recursive_backtracker")

    val colorization1 = Colorization(grid).dijkstra(grid[20, 10]!!)
    saveImage(grid.toImage(backgroundColors = colorization1), "hex_recursive_backtracker_colorized1")

    val colorization2 = Colorization(grid).countLinks()
    saveImage(grid.toImage(backgroundColors = colorization2), "hex_recursive_backtracker_colorized2")
}
