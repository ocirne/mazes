package io.github.ocirne.mazes.algorithms

import io.github.ocirne.mazes.grids.CartesianCell
import io.github.ocirne.mazes.grids.CartesianGrid
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.math.abs

/** cells can only be linked to neighbors */
class LinkageTest {

    private val size = 10

    @Test
    fun `Cartesian grid with Wilsons maze has only neighbors as links`() {
        val grid = CartesianGrid(size, size)
        Wilsons().on(grid)

        for (cell in grid.eachCell()) {
            val row = cell.row
            val col = cell.column

            for (linked in cell.links()) {
                linked as CartesianCell
                abs(linked.row - row) + abs(linked.column - col) shouldBe 1
            }
        }
    }
}
