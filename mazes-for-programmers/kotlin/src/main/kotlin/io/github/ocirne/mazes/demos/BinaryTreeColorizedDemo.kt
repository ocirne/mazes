package io.github.ocirne.mazes.demos

import io.github.ocirne.mazes.algorithms.BinaryTree
import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.grids.cartesian.CartesianGrid
import io.github.ocirne.mazes.output.saveAs


fun main() {
    val grid = CartesianGrid(21, 21)
    val maze = BinaryTree().on(grid)

    val colorization = Colorization(maze).dijkstra(maze[20, 10]!!)
    maze.toImage(backgroundColors = colorization).saveAs("cartesian_binarytree_colored")
}
