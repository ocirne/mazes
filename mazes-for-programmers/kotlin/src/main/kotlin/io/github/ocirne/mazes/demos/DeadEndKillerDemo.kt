package io.github.ocirne.mazes.demos

import io.github.ocirne.mazes.algorithms.AldousBroder
import io.github.ocirne.mazes.algorithms.DeadEndKiller
import io.github.ocirne.mazes.grids.CartesianGrid
import io.github.ocirne.mazes.colorization.CountLinks
import io.github.ocirne.mazes.output.saveImage

fun main() {
    val grid = CartesianGrid(21, 21)
    AldousBroder().on(grid)

    DeadEndKiller().remove(grid, passes=5, p=0.5)
    saveImage(grid.toImage(), "cartesian_dead_ends_removed")

    val colorization2 = CountLinks(grid)
    saveImage(grid.toImage(colorization = colorization2), "cartesian_dead_ends_removed_colored")
}
