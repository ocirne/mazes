package io.github.ocirne.mazes.demos

import io.github.ocirne.mazes.algorithms.BinaryTree
import io.github.ocirne.mazes.grids.cartesian.CartesianGrid
import io.github.ocirne.mazes.grids.cartesian.createImage
import io.github.ocirne.mazes.grids.cartesian.saveAs


fun main() {
    val grid = CartesianGrid(4, 4)
    val maze = BinaryTree().on(grid)
    maze.createImage().saveAs("cartesian_binarytree")
}
