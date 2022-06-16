package io.github.ocirne.mazes


fun main() {
    val grid = Grid(4, 4)
    BinaryTree.on(grid)

    val image = grid.toImage()
    saveImage(image, "binary_tree")
}
