package io.github.ocirne.mazes.demos

import io.github.ocirne.mazes.algorithms.AldousBroder
import io.github.ocirne.mazes.algorithms.DeadEndKiller
import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.grids.cartesian.CartesianGrid
import io.github.ocirne.mazes.output.saveImage
import java.awt.Color

fun main() {
    val grid = CartesianGrid(11, 11)
    val maze = AldousBroder().on(grid)

    DeadEndKiller().remove(maze, passes = 5, p = 0.5)
    saveImage(maze.toImage(wallInset = 0.1), "cartesian_dead_ends_removed")

    val colorization2 = Colorization(maze, fromColor = Color.LIGHT_GRAY, toColor = Color.DARK_GRAY).dijkstra(maze.randomCell(noNeighborsAllowed = false))
    saveImage(maze.toImage(wallInset = 0.1, backgroundColors = colorization2), "cartesian_dead_ends_removed_colored")

    saveImage(maze.toImage(wallInset = 0.2, backInset = 0.1, backgroundColors = colorization2), "cartesian_dead_ends_removed_colored_insets")
}
