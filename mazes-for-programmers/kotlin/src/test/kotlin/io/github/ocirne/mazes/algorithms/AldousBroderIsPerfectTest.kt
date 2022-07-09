package io.github.ocirne.mazes.algorithms

import io.github.ocirne.mazes.grids.*
import org.junit.jupiter.api.Test

class AldousBroderIsPerfectTest {

    private val size = 10

    @Test
    fun `Cartesian Grid with AldousBroder maze is perfect`() {
        val grid = CartesianGrid(size, size)
        AldousBroder().on(grid)

        CycleDetection(grid).assertNoCycle()
    }

    @Test
    fun `Polar Grid with AldousBroder maze is perfect`() {
        val grid = PolarGrid(size)
        AldousBroder().on(grid)

        CycleDetection(grid).assertNoCycle()
    }

    @Test
    fun `Triangle Grid with AldousBroder maze is perfect`() {
        val grid = TriangleGrid(size, size)
        AldousBroder().on(grid)

        CycleDetection(grid).assertNoCycle()
    }

    @Test
    fun `Hex Grid with AldousBroder maze is perfect`() {
        val grid = HexGrid(size, size)
        AldousBroder().on(grid)

        CycleDetection(grid).assertNoCycle()
    }

    @Test
    fun `Upsilon Grid with AldousBroder maze is perfect`() {
        val grid = UpsilonGrid(size, size)
        AldousBroder().on(grid)

        CycleDetection(grid).assertNoCycle()
    }
}