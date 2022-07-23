package io.github.ocirne.mazes.demos

import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.algorithms.RecursiveBacktracker
import io.github.ocirne.mazes.grids.TriangleGrid
import io.github.ocirne.mazes.output.saveImage

// also DeltaMaze
fun main() {
    val grid = TriangleGrid(11, 18)
    val maze = RecursiveBacktracker().on(grid)
    saveImage(maze.toImage(), "triangle_recursive_backtracker")

    val colorization1 = Colorization(maze).dijkstra(maze[9, 8]!!)
    saveImage(maze.toImage(backgroundColors = colorization1), "triangle_recursive_backtracker_colorized1")

    val colorization2 = Colorization(maze).countLinks()
    saveImage(maze.toImage(backgroundColors = colorization2), "triangle_recursive_backtracker_colorized2")
}
