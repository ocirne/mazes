package io.github.ocirne.mazes

fun main() {
    val grid = PolarGrid(8)
    RecursiveBacktracker.on(grid)

    val image = grid.toImage()
    saveImage(image, "polar")
}
