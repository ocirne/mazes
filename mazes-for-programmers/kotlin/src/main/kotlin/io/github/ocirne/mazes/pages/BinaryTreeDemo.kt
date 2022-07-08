package io.github.ocirne.mazes.pages

import io.github.ocirne.mazes.algorithms.BinaryTree
import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.grids.*
import io.github.ocirne.mazes.output.formatForPages
import io.github.ocirne.mazes.output.save

fun allGridsWith(algorithm: BinaryTree) {
    val baseSize = 20.0
    val cartesianGrid = CartesianGrid(11, 11)
    algorithm.on(cartesianGrid)
    val cartesianImage = cartesianGrid.toImage(baseSize)

    val polarGrid = PolarGrid(6)
    algorithm.on(polarGrid)
    val polarImage = polarGrid.toImage(baseSize)

    val triangleGrid = TriangleGrid(8, 13)
    algorithm.on(triangleGrid)
    val triangleImage = triangleGrid.toImage(baseSize)

    val hexGrid = HexGrid(10, 11)
    algorithm.on(hexGrid)
    val hexImage = hexGrid.toImage(baseSize)

    val upsilonGrid = UpsilonGrid(11, 11)
    algorithm.on(upsilonGrid)
    val upsilonImage = upsilonGrid.toImage(baseSize)

    formatForPages(listOf(cartesianImage, polarImage, triangleImage, hexImage, upsilonImage), 3, 2)
        .save("all_grids_${algorithm::class.simpleName}")
}

fun cartesianBinaryTreeColorized() {
    val grids = (1.. 6).map {
        val grid = CartesianGrid(15, 15)
        BinaryTree().on(grid)
        val colorization = Colorization(grid).dijkstra(grid[7, 7]!!)
        grid.toImage(baseSize = 15.0, backgroundColors = colorization)
    }
    formatForPages(grids, 3, 2).save("cartesian_binarytrees_colorized")
}

fun main() {
    allGridsWith(BinaryTree())
    cartesianBinaryTreeColorized()
}
