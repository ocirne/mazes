package io.github.ocirne.mazes.demos

import io.github.ocirne.mazes.algorithms.BinaryTree
import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.grids.UpsilonGrid
import io.github.ocirne.mazes.output.saveImage

fun main() {
    val grid = UpsilonGrid(21, 21)

    BinaryTree().on(grid)
    saveImage(grid.toImage(), "upsilon_binarytree")

    val colorization1 = Colorization(grid).dijkstra(grid[20, 10]!!)
    saveImage(grid.toImage(backgroundColors = colorization1), "upsilon_binarytree_colorized1")

    val colorization2 = Colorization(grid).countLinks()
    saveImage(grid.toImage(backgroundColors = colorization2), "upsilon_binarytree_colorized2")
}
