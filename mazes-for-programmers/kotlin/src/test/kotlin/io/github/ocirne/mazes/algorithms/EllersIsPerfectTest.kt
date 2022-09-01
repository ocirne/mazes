package io.github.ocirne.mazes.algorithms

import io.github.ocirne.mazes.grids.cartesian.CartesianGrid
import io.github.ocirne.mazes.grids.polar.PolarGrid
import org.junit.jupiter.api.Test

class EllersIsPerfectTest {

    private val size = 10

    @Test
    fun `Cartesian Grid with Ellers maze is perfect`() {
        val grid = CartesianGrid(size, size)
        val maze = Ellers().on(grid)

        CycleDetection(maze).assertNoCycle()
    }

    @Test
    fun `Polar Grid with Ellers maze is perfect`() {
        val grid = PolarGrid(size)
        val maze = Ellers().on(grid)

        CycleDetection(maze).assertNoCycle()
    }
}