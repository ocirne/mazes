package io.github.ocirne.mazes.demos

import io.github.ocirne.mazes.colorization.DijkstraDistances
import io.github.ocirne.mazes.algorithms.RecursiveBacktracker
import io.github.ocirne.mazes.colorization.CountLinks
import io.github.ocirne.mazes.grids.HexGrid
import io.github.ocirne.mazes.output.saveImage

fun main() {
    val grid = HexGrid(21, 21)
    saveImage(grid.toImage(), "hex")

    RecursiveBacktracker.on(grid)
    saveImage(grid.toImage(), "hex_recursive_backtracker")

    val colorization1 = DijkstraDistances(grid, startAt=grid[20, 10]!!)
    saveImage(grid.toImage(colorization = colorization1), "hex_recursive_backtracker_colorized1")

    val colorization2 = CountLinks(grid)
    saveImage(grid.toImage(colorization = colorization2), "hex_recursive_backtracker_colorized2")
}
