package io.github.ocirne.mazes.demos

import io.github.ocirne.mazes.algorithms.BinaryTree
import io.github.ocirne.mazes.colorization.Distances
import io.github.ocirne.mazes.grids.CartesianGrid
import io.github.ocirne.mazes.saveImage


fun main() {
    val grid = CartesianGrid(4, 4)

    BinaryTree.on(grid)
    saveImage(grid.toImage(), "cartesian_binarytree")

    val colorization = Distances.doing(grid)
    saveImage(grid.toImage(colorization = colorization), "cartesian_binarytree_colorized")
}
