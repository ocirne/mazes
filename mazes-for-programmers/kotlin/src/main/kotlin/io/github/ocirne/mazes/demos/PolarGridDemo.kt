package io.github.ocirne.mazes.demos

import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.grids.PolarGrid
import io.github.ocirne.mazes.algorithms.RecursiveBacktracker
import io.github.ocirne.mazes.output.saveImage
import java.awt.Color

fun main() {
    val grid = PolarGrid(21)
    saveImage(grid.toImage(), "polar")
    saveImage(grid.toImage(wallInset=0.2), "polar_inset")

    RecursiveBacktracker.on(grid)
    saveImage(grid.toImage(), "polar_recursive_backtracker")
    saveImage(grid.toImage(wallInset=0.1), "polar_recursive_backtracker_inset_1")
    saveImage(grid.toImage(wallInset=0.2), "polar_recursive_backtracker_inset_2")

    val colorization = Colorization(grid, startAt=grid[20, 0]!!, fromColor = Color.RED, toColor = Color.ORANGE).dijkstra()
    saveImage(grid.toImage(colorization = colorization), "polar_recursive_backtracker_colorized")
    saveImage(grid.toImage(wallInset=0.2, backInset=0.1, colorization = colorization), "polar_recursive_backtracker_colorized_inset")

    val longestPath = Colorization(grid).longestPath()
    saveImage(grid.toImage(wallInset=0.1, backInset = 0.4, colorization = longestPath), "polar_recursive_backtracker_longest_path")
}
