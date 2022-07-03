package io.github.ocirne.mazes.demos

import io.github.ocirne.mazes.algorithms.BinaryTree
import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.grids.CartesianGrid
import io.github.ocirne.mazes.output.save


fun main() {
    (1.. 3).forEach { i ->
        val grid = CartesianGrid(21, 21)
        BinaryTree().on(grid)
        grid.toImage().save("cartesian_binarytree_$i")

        val colorization = Colorization(grid).dijkstra(grid[20, 10]!!)
        grid.toImage(backgroundColors = colorization).save("cartesian_binarytree_${i}_colorized")
    }
}
