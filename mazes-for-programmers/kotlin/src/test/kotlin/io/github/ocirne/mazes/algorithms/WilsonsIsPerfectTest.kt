package io.github.ocirne.mazes.algorithms

import io.github.ocirne.mazes.grids.*
import org.junit.jupiter.api.Test

class WilsonsIsPerfectTest {

    private val size = 10

    @Test
    fun `Cartesian Grid with Wilsons maze is perfect`() {
        val grid = CartesianGrid(size, size)
        Wilsons().on(grid)

        CycleDetection(grid).assertNoCycle()
    }

    @Test
    fun `Polar Grid with Wilsons maze is perfect`() {
        val grid = PolarGrid(size)
        Wilsons().on(grid)

        CycleDetection(grid).assertNoCycle()
    }

    @Test
    fun `Triangle Grid with Wilsons maze is perfect`() {
        val grid = TriangleGrid(size, size)
        Wilsons().on(grid)

        CycleDetection(grid).assertNoCycle()
    }

    @Test
    fun `Hex Grid with Wilsons maze is perfect`() {
        val grid = HexGrid(size, size)
        Wilsons().on(grid)

        CycleDetection(grid).assertNoCycle()
    }

    @Test
    fun `Upsilon Grid with Wilsons maze is perfect`() {
        val grid = UpsilonGrid(size, size)
        Wilsons().on(grid)

        CycleDetection(grid).assertNoCycle()
    }
}