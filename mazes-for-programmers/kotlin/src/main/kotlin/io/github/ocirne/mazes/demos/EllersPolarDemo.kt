package io.github.ocirne.mazes.demos

import io.github.ocirne.mazes.algorithms.Ellers
import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.grids.polar.PolarGrid
import io.github.ocirne.mazes.output.save


fun main() {
    val grid = PolarGrid(21)
    val maze = Ellers().on(grid)

    val colorization = Colorization(maze).dijkstra(maze[0, 0]!!)
    maze.toImage(backgroundColors = colorization).save("polar_ellers_colorized")
}
