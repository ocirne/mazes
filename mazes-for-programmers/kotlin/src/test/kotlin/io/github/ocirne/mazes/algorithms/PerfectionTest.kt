package io.github.ocirne.mazes.algorithms

import io.github.ocirne.mazes.grids.*
import org.junit.jupiter.api.Test

/** from every cell all other cells are reachable in exactly one path */
internal class PerfectionTest {

    // TODO add seed feedback to reproduce errors

    private val size = 10

    @Test
    fun `Cartesian grid with Sidewinder is perfect`() {
        val grid = CartesianGrid(size, size)
        val maze = Sidewinder().on(grid)

        CycleDetection(maze).assertNoCycle()
    }
}