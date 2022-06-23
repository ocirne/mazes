package io.github.ocirne.mazes.demos

import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.algorithms.RecursiveBacktracker
import io.github.ocirne.mazes.grids.TriangleGrid
import io.github.ocirne.mazes.output.saveImage

// also DeltaMaze
fun main() {
    val grid = TriangleGrid(20, 33)
    saveImage(grid.toImage(), "triangle")

    RecursiveBacktracker.on(grid)
    saveImage(grid.toImage(), "triangle_recursive_backtracker")

    val colorization1 = Colorization(grid, startAt=grid[9, 8]!!).dijkstra()
    saveImage(grid.toImage(backgroundColors = colorization1), "triangle_recursive_backtracker_colorized1")

    val colorization2 = Colorization(grid).countLinks()
    saveImage(grid.toImage(backgroundColors = colorization2), "triangle_recursive_backtracker_colorized2")
}
