package io.github.ocirne.mazes

fun main() {
    val grid = Grid(20, 20)
    RecursiveBacktracker.on(grid)

    val image = grid.toImage()
    saveImage(image, "recursive_backtracker")
}
