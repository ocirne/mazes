package io.github.ocirne.mazes.demos

import io.github.ocirne.mazes.algorithms.BinaryTree
import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.grids.*
import io.github.ocirne.mazes.output.combineToImage
import io.github.ocirne.mazes.output.save

fun allGridsWith(algorithm: BinaryTree) {
    val baseSize = 15.0
    val cartesianGrid = CartesianGrid(15, 15)
    algorithm.on(cartesianGrid)
    val cartesianImage = cartesianGrid.toImage(baseSize = baseSize)

    val polarGrid = PolarGrid(5)
    algorithm.on(polarGrid)
    val polarImage = polarGrid.toImage(baseSize = baseSize)

    val triangleGrid = TriangleGrid(8, 13)
    algorithm.on(triangleGrid)
    val triangleImage = triangleGrid.toImage(baseSize = baseSize)

    val hexGrid = HexGrid(9, 11)
    algorithm.on(hexGrid)
    val hexImage = hexGrid.toImage(baseSize = baseSize)

    val upsilonGrid = UpsilonGrid(11, 11)
    algorithm.on(upsilonGrid)
    val upsilonImage = upsilonGrid.toImage(baseSize = baseSize)

    combineToImage(listOf(cartesianImage, polarImage, triangleImage, hexImage, upsilonImage))
        .save("all_grids_${algorithm::class.simpleName}")
}

fun cartesianBinaryTreeColorized() {
    val grids = (1.. 6).map {
        val grid = CartesianGrid(15, 15)
        BinaryTree().on(grid)
        val colorization = Colorization(grid).dijkstra(grid[7, 7]!!)
        grid.toImage(baseSize = 15.0, backgroundColors = colorization)
    }
    combineToImage(grids).save("cartesian_binarytrees_colorized")
}

fun main() {
    allGridsWith(BinaryTree())
    cartesianBinaryTreeColorized()
}
