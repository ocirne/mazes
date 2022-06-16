package io.github.ocirne.mazes

fun main() {
    val grid = PolarGrid(8)
    RecursiveBacktracker.on(grid)

    val start = grid[0, 0]
    val distances = start!!.distances()
    grid.distances = distances

    val image = grid.toImage()
    saveImage(image, "polar")
}
