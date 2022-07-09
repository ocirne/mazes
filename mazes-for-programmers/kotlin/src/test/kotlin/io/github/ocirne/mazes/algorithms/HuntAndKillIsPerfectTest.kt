package io.github.ocirne.mazes.algorithms

import io.github.ocirne.mazes.grids.*
import org.junit.jupiter.api.Test

class HuntAndKillIsPerfectTest {

    private val size = 10

    @Test
    fun `Cartesian Grid with HuntAndKill maze is perfect`() {
        val grid = CartesianGrid(size, size)
        HuntAndKill().on(grid)

        CycleDetection(grid).assertNoCycle()
    }

    @Test
    fun `Polar Grid with HuntAndKill maze is perfect`() {
        val grid = PolarGrid(size)
        HuntAndKill().on(grid)

        CycleDetection(grid).assertNoCycle()
    }

    @Test
    fun `Triangle Grid with HuntAndKill maze is perfect`() {
        val grid = TriangleGrid(size, size)
        HuntAndKill().on(grid)

        CycleDetection(grid).assertNoCycle()
    }

    @Test
    fun `Hex Grid with HuntAndKill maze is perfect`() {
        val grid = HexGrid(size, size)
        HuntAndKill().on(grid)

        CycleDetection(grid).assertNoCycle()
    }

    @Test
    fun `Upsilon Grid with HuntAndKill maze is perfect`() {
        val grid = UpsilonGrid(size, size)
        HuntAndKill().on(grid)

        CycleDetection(grid).assertNoCycle()
    }
}