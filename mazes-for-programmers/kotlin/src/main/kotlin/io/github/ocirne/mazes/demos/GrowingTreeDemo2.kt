package io.github.ocirne.mazes.demos

import io.github.ocirne.mazes.algorithms.*
import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.grids.cartesian.CartesianGrid
import io.github.ocirne.mazes.grids.hex.HexGrid
import io.github.ocirne.mazes.grids.polar.PolarGrid
import io.github.ocirne.mazes.grids.triangle.TriangleGrid
import io.github.ocirne.mazes.grids.upsilon.UpsilonGrid
import io.github.ocirne.mazes.output.formatForPages
import io.github.ocirne.mazes.output.save
import kotlin.random.Random.Default.nextBoolean

fun allGridsWith(marker: String, algorithm: GrowingTree) {
    val grids = listOf(
        CartesianGrid(11, 11),
        PolarGrid(6),
        TriangleGrid(8, 13),
        HexGrid(10, 11),
        UpsilonGrid(11, 11)
    )
    val images = grids.map { grid ->
        val maze = algorithm.on(grid, startAt={it[0, 0]!!})
        val colorization = Colorization(maze).dijkstra(maze[0, 0]!!)
        maze.toImage(baseSize = 20.0, backgroundColors = colorization)
    }
    formatForPages(images, 3, 2).save("all_grids_${algorithm::class.simpleName}_${marker}_colorized")
}

fun main() {
    allGridsWith("random", GrowingTree { list -> list.random() })
    allGridsWith("last", GrowingTree { list -> list.last() })
    allGridsWith("mix", GrowingTree { list -> if (nextBoolean()) list.last() else list.random() })
}
