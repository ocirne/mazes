package io.github.ocirne.mazes.demos

import io.github.ocirne.mazes.algorithms.GrowingTree
import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.grids.CartesianGrid
import io.github.ocirne.mazes.grids.Cell
import io.github.ocirne.mazes.output.saveImage
import kotlin.random.Random.Default.nextBoolean

private fun wrap(name: String, f : (List<Cell>) -> Cell) {
    val grid = CartesianGrid(21, 21)
    GrowingTree.on(grid, f, startAt=grid[20, 10]!!)
    saveImage(grid.toImage(), "cartesian_growingTree_$name")

    val colorization = Colorization(grid).dijkstra(grid[20, 10]!!)
    saveImage(grid.toImage(backgroundColors = colorization), "cartesian_growingTree_${name}_colorized")
}

fun main() {
    wrap("random") { list -> list.random() }
    wrap("last") { list -> list.last() }
    wrap("mix") { list -> if (nextBoolean()) list.last() else list.random() }
}
