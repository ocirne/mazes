package io.github.ocirne.mazes.demos

import io.github.ocirne.mazes.algorithms.RecursiveBacktracker
import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.grids.PolarWeaveGrid
import io.github.ocirne.mazes.output.saveImage

fun main() {
    val grid = PolarWeaveGrid(10)
    RecursiveBacktracker().on(grid)

//    DeadEndKiller().remove(grid, passes=10, p=0.5)

    val backgrounds = Colorization(grid).dijkstra()

    saveImage(
        grid.toImage(
            wallInset = 0.2,
            backInset = 0.1,
            backgroundColors = backgrounds
        ), "polar_weave_recursive_backtracker"
    )
}
