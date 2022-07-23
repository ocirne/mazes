package io.github.ocirne.mazes.demos

import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.algorithms.RecursiveBacktracker
import io.github.ocirne.mazes.grids.PolarGrid
import io.github.ocirne.mazes.output.saveImage
import java.awt.Color

fun main() {
    val grid = PolarGrid(10)
    saveImage(grid.toImage(wallInset=0.2), "polar_inset")

    val maze = RecursiveBacktracker().on(grid)
    saveImage(maze.toImage(), "polar_recursive_backtracker")
    saveImage(maze.toImage(wallInset=0.1), "polar_recursive_backtracker_inset_1")
    saveImage(maze.toImage(wallInset=0.2), "polar_recursive_backtracker_inset_2")

    val colorization = Colorization(maze, fromColor = Color.LIGHT_GRAY, toColor = Color.LIGHT_GRAY).dijkstra(maze[2, 0]!!)
    saveImage(maze.toImage(backgroundColors = colorization), "polar_recursive_backtracker_colorized")
    saveImage(maze.toImage(wallInset=0.2, backInset=0.1, backgroundColors = colorization), "polar_recursive_backtracker_colorized_inset")

    val longestPath = Colorization(maze).longestPath()
    val markers = Colorization(maze)
    markers[longestPath.start!!] = "A"
    markers[longestPath.goal!!] = "B"
    saveImage(maze.toImage(wallInset=0.2, backInset = 0.1,
        backgroundColors = colorization,
        path = longestPath,
        marker = markers), "polar_recursive_backtracker_longest_path")
}
