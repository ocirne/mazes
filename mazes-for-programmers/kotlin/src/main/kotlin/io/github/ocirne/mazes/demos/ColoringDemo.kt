package io.github.ocirne.mazes.demos

import io.github.ocirne.mazes.algorithms.BinaryTree
import io.github.ocirne.mazes.algorithms.PassageCarver
import io.github.ocirne.mazes.algorithms.Sidewinder
import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.grids.cartesian.CartesianGrid
import io.github.ocirne.mazes.grids.cartesian.createImage
import io.github.ocirne.mazes.grids.cartesian.saveAs

fun coloringDemo(algorithm: PassageCarver, name: String) {
    val grid = CartesianGrid(11, 11)
    val maze = algorithm.on(grid)

    val colorization = Colorization(maze).dijkstra(maze[5, 5]!!)
    maze.createImage()
        .withBackgroundColors(colorization)
        .saveAs("cartesian_${name}_colored")
}

fun binarytreeColored() {
    coloringDemo(BinaryTree(), "binarytree")
}

fun sidewinderColored() {
    coloringDemo(Sidewinder(), "sidewinder")
}

fun main() {
    binarytreeColored()
    sidewinderColored()
}
