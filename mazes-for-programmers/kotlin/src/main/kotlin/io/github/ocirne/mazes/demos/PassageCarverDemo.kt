package io.github.ocirne.mazes.pages

import io.github.ocirne.mazes.algorithms.*
import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.grids.*
import io.github.ocirne.mazes.grids.cartesian.CartesianGrid
import io.github.ocirne.mazes.grids.hex.HexGrid
import io.github.ocirne.mazes.grids.polar.PolarGrid
import io.github.ocirne.mazes.grids.triangle.TriangleGrid
import io.github.ocirne.mazes.grids.upsilon.UpsilonGrid
import io.github.ocirne.mazes.output.formatForPages
import io.github.ocirne.mazes.output.saveAs

fun createAllGrids(): List<GridProvider> {
    return listOf(
        CartesianGrid(11, 11),
        PolarGrid(6),
        TriangleGrid(8, 13),
        HexGrid(10, 11),
        UpsilonGrid(11, 11)
    )
}

fun allGridsPlain() {
    val images = createAllGrids().map { it.forPassageCarver().toImage(baseSize = 20.0) }
    formatForPages(images, 3, 2).saveAs("all_grids_plain")
}

fun allGridsWith(algorithm: PassageCarver) {
    val images = createAllGrids().map { grid ->
        val maze = algorithm.on(grid)
        maze.toImage(baseSize = 20.0)
    }
    formatForPages(images, 3, 2).saveAs("all_grids_${algorithm::class.simpleName}")
}

fun cartesianBinaryTreeColored(algorithm: PassageCarver) {
    val images = (1..6).map {
        val grid = CartesianGrid(15, 15)
        val maze = algorithm.on(grid)
        val colorization = Colorization(maze).dijkstra(maze[7, 7]!!)
        maze.toImage(baseSize = 15.0, backgroundColors = colorization)
    }
    formatForPages(images, 3, 2).saveAs("cartesian_${algorithm::class.simpleName}_colored")
}

fun main() {
    allGridsPlain()
    // TODO Sidewinder
    val passageCarvers = listOf(AldousBroder(), BinaryTree(), HuntAndKill(), RecursiveBacktracker(), Wilsons())
    passageCarvers.forEach {
        allGridsWith(it)
        cartesianBinaryTreeColored(it)
    }
}
