package io.github.ocirne.mazes.demos

import io.github.ocirne.mazes.algorithms.BinaryTree
import io.github.ocirne.mazes.colorization.DijkstraDistances
import io.github.ocirne.mazes.grids.CartesianGrid
import io.github.ocirne.mazes.output.saveImage


fun main() {
    val grid = CartesianGrid(21, 21)

    BinaryTree().on(grid)
    saveImage(grid.toImage(), "cartesian_binarytree")

    val colorization = DijkstraDistances(grid, startAt=grid[20, 10]!!)
    saveImage(grid.toImage(colorization = colorization), "cartesian_binarytree_colorized")
}
