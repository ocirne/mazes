package io.github.ocirne.mazes

fun main() {
    val grid = Grid(20, 20)
    RecursiveBacktracker.on(grid)

    val start = grid[0, 0]
    val distances = start!!.distances()
    grid.distances = distances

    val image = grid.toImage()
    saveImage(image, "recursive_backtracker")
}
