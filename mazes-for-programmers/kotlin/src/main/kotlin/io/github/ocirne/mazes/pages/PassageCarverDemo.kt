package io.github.ocirne.mazes.pages

import io.github.ocirne.mazes.algorithms.*
import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.grids.*
import io.github.ocirne.mazes.output.formatForPages
import io.github.ocirne.mazes.output.save

fun allGridsWith(algorithm: PassageCarver) {
    val grids = listOf(
        CartesianGrid(11, 11),
        PolarGrid(6),
        TriangleGrid(8, 13),
        HexGrid(10, 11),
        UpsilonGrid(11, 11)
    )
    val images = grids.map { grid ->
        algorithm.on(grid)
        grid.toImage(baseSize = 20.0)
    }
    formatForPages(images, 3, 2).save("all_grids_${algorithm::class.simpleName}")
}

fun cartesianBinaryTreeColorized(algorithm: PassageCarver) {
    val images = (1..6).map {
        val grid = CartesianGrid(15, 15)
        algorithm.on(grid)
        val colorization = Colorization(grid).dijkstra(grid[7, 7]!!)
        grid.toImage(baseSize = 15.0, backgroundColors = colorization)
    }
    formatForPages(images, 3, 2).save("cartesian_${algorithm::class.simpleName}_colorized")
}

fun main() {
    val passageCarvers = listOf(AldousBroder(), BinaryTree(), RecursiveBacktracker(), Wilsons())
    passageCarvers.forEach {
        allGridsWith(it)
        cartesianBinaryTreeColorized(it)
    }
}
