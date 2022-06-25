package io.github.ocirne.mazes.demos

import io.github.ocirne.mazes.algorithms.RecursiveBacktracker
import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.grids.WeaveGrid
import io.github.ocirne.mazes.output.saveImage

fun main() {
    val grid = WeaveGrid(21, 21)
    RecursiveBacktracker.on(grid)

    val colorization = Colorization(grid).dijkstra()

    saveImage(
        grid.toImage(
            wallInset = 0.2,
            backInset = 0.2,
            backgroundColors = colorization
        ), "cartesian_weave_recursive_backtracker"
    )
}
