package io.github.ocirne.mazes.demos

import io.github.ocirne.mazes.algorithms.BinaryTree
import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.grids.CartesianGrid
import io.github.ocirne.mazes.output.saveImage


fun main() {
    val grid = CartesianGrid(11, 11)

    BinaryTree().on(grid)
    saveImage(grid.toImage(), "cartesian_binarytree")

    val colorization = Colorization(grid).dijkstra(grid[10, 5]!!)
    saveImage(grid.toImage(backgroundColors = colorization), "cartesian_binarytree_colorized")
}
