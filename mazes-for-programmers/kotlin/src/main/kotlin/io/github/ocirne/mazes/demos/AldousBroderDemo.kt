package io.github.ocirne.mazes.demos

import io.github.ocirne.mazes.algorithms.AldousBroder
import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.grids.CartesianGrid
import io.github.ocirne.mazes.output.saveImage


fun main() {
    val grid = CartesianGrid(21, 21)

    AldousBroder().on(grid)
    saveImage(grid.toImage(), "cartesian_aldous_broder")

    val colorization = Colorization(grid, startAt=grid[20, 10]!!).dijkstra()
    saveImage(grid.toImage(colorization = colorization), "cartesian_aldous_broder_colorized")
}
