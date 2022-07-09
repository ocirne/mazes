package io.github.ocirne.mazes.pages

import io.github.ocirne.mazes.algorithms.*
import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.grids.*
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
        algorithm.on(grid, startAt=grid[0, 0]!!)
        val colorization = Colorization(grid).dijkstra(grid[0, 0]!!)
        grid.toImage(baseSize = 20.0, backgroundColors = colorization)
    }
    formatForPages(images, 3, 2).save("all_grids_${algorithm::class.simpleName}_${marker}_colorized")
}

fun main() {
    allGridsWith("random", GrowingTree { list -> list.random() })
    allGridsWith("last", GrowingTree { list -> list.last() })
    allGridsWith("mix", GrowingTree { list -> if (nextBoolean()) list.last() else list.random() })
}
