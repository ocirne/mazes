package io.github.ocirne.mazes.demos

import io.github.ocirne.mazes.colorization.DijkstraDistances
import io.github.ocirne.mazes.algorithms.RecursiveBacktracker
import io.github.ocirne.mazes.colorization.CountLinks
import io.github.ocirne.mazes.grids.TriangleGrid
import io.github.ocirne.mazes.output.saveImage

// also DeltaMaze
fun main() {
    val grid = TriangleGrid(10, 17)
    saveImage(grid.toImage(), "triangle")

    RecursiveBacktracker.on(grid)
    saveImage(grid.toImage(), "triangle_recursive_backtracker")

    val colorization1 = DijkstraDistances(grid, startAt=grid[9, 8]!!)
    saveImage(grid.toImage(colorization = colorization1), "triangle_recursive_backtracker_colorized1")

    val colorization2 = CountLinks(grid)
    saveImage(grid.toImage(colorization = colorization2), "triangle_recursive_backtracker_colorized2")
}
