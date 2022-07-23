package io.github.ocirne.mazes.algorithms

import io.github.ocirne.mazes.grids.*
import org.junit.jupiter.api.Test

class RecursiveDivisionIsPerfectTest {

    private val size = 10

    @Test
    fun `Cartesian Grid with RecursiveDivision maze is perfect`() {
        val grid = CartesianGrid(size, size)
        val maze = RecursiveDivision(rooms = false).on(grid)

        CycleDetection(maze).assertNoCycle()
    }
}
