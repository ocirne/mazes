package io.github.ocirne.mazes.demos

import io.github.ocirne.mazes.algorithms.GrowingTree
import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.grids.cartesian.CartesianGrid
import io.github.ocirne.mazes.grids.Cell
import io.github.ocirne.mazes.grids.GridProvider
import io.github.ocirne.mazes.output.saveImage
import kotlin.random.Random.Default.nextBoolean

private fun wrap(grid: GridProvider, name: String, f : (List<Cell>) -> Cell) {
    val maze = GrowingTree(f).on(grid, startAt={ it[10, 5]!! })
    saveImage(maze.toImage(), "cartesian_growingTree_$name")

    val colorization = Colorization(maze).dijkstra(maze[10, 5]!!)
    saveImage(maze.toImage(backgroundColors = colorization), "cartesian_growingTree_${name}_colorized")
}

fun main() {
    val grid = CartesianGrid(11, 11)
    wrap(grid, "random") { list -> list.random() }
    wrap(grid, "last") { list -> list.last() }
    wrap(grid, "mix") { list -> if (nextBoolean()) list.last() else list.random() }
}
