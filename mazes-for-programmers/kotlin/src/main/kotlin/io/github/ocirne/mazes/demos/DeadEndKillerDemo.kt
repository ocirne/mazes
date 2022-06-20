package io.github.ocirne.mazes.demos

import io.github.ocirne.mazes.algorithms.AldousBroder
import io.github.ocirne.mazes.algorithms.DeadEndKiller
import io.github.ocirne.mazes.grids.CartesianGrid
import io.github.ocirne.mazes.colorization.DijkstraDistances
import io.github.ocirne.mazes.output.saveImage
import java.awt.Color

fun main() {
    val grid = CartesianGrid(21, 21)
    AldousBroder().on(grid)

    DeadEndKiller().remove(grid, passes=5, p=0.5)
    saveImage(grid.toImage(), "cartesian_dead_ends_removed")

    val colorization2 = DijkstraDistances(grid, fromColor = Color.LIGHT_GRAY, toColor = Color.DARK_GRAY)
    saveImage(grid.toImage(colorization = colorization2), "cartesian_dead_ends_removed_colored")

    saveImage(grid.toImage(inset=0.2, colorization = colorization2), "cartesian_dead_ends_removed_colored_insets")
}
