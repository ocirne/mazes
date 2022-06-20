package io.github.ocirne.mazes.demos

import io.github.ocirne.mazes.algorithms.AldousBroder
import io.github.ocirne.mazes.algorithms.DeadEndKiller
import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.grids.CartesianGrid
import io.github.ocirne.mazes.output.saveImage
import java.awt.Color

fun main() {
    val grid = CartesianGrid(21, 21)
    AldousBroder().on(grid)

    DeadEndKiller().remove(grid, passes=5, p=0.5)
    saveImage(grid.toImage(), "cartesian_dead_ends_removed")

    val colorization2 = Colorization(grid, startAt=grid.randomCell(noNeighborsAllowed = false), fromColor = Color.LIGHT_GRAY, toColor = Color.DARK_GRAY).dijkstra()
    saveImage(grid.toImage(colorization = colorization2), "cartesian_dead_ends_removed_colored")

    saveImage(grid.toImage(wallInset=0.2, backInset = 0.1, colorization = colorization2), "cartesian_dead_ends_removed_colored_insets")
}
