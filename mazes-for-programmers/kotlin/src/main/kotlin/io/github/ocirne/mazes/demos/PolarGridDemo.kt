package io.github.ocirne.mazes.demos

import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.grids.PolarGrid
import io.github.ocirne.mazes.algorithms.RecursiveBacktracker
import io.github.ocirne.mazes.output.saveImage
import java.awt.Color

fun main() {
    val grid = PolarGrid(21)
    saveImage(grid.toImage(), "polar")

    RecursiveBacktracker.on(grid)
    saveImage(grid.toImage(), "polar_recursive_backtracker")

    val colorization = Colorization(grid, startAt=grid[20, 0]!!, fromColor = Color.RED, toColor = Color.ORANGE).dijkstra()
    saveImage(grid.toImage(colorization = colorization), "polar_recursive_backtracker_colorized")
}
