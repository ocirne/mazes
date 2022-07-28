package io.github.ocirne.mazes.algorithms

import io.github.ocirne.mazes.grids.cartesian.CartesianGrid
import io.github.ocirne.mazes.grids.cartesian.CartesianGrid.CartesianCell
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.math.abs

/** cells can only be linked to neighbors */
class LinkageTest {

    private val size = 10

    @Test
    fun `Cartesian grid with Wilsons maze has only neighbors as links`() {
        val grid = CartesianGrid(size, size)
        val maze = Wilsons().on(grid)

        for (cell in maze.eachCell()) {
            cell as CartesianCell
            val row = cell.row
            val col = cell.column

            for (linked in cell.links()) {
                linked as CartesianCell
                abs(linked.row - row) + abs(linked.column - col) shouldBe 1
            }
        }
    }
}
