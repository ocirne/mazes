package io.github.ocirne.mazes.demos

import io.github.ocirne.mazes.algorithms.Sidewinder
import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.grids.cartesian.CartesianGrid
import io.github.ocirne.mazes.output.save


fun main() {
    (1.. 3).forEach { i ->
        val grid = CartesianGrid(21, 21)
        val maze = Sidewinder().on(grid)
        maze.toImage().save("cartesian_sidewinder_$i")

        val colorization = Colorization(maze).dijkstra(maze[20, 10]!!)
        maze.toImage(backgroundColors = colorization).save("cartesian_sidewinder_${i}_colorized")
    }
}
