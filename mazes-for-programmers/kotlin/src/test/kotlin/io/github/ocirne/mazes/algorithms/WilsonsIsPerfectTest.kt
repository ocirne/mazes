package io.github.ocirne.mazes.algorithms

import io.github.ocirne.mazes.grids.cartesian.CartesianGrid
import io.github.ocirne.mazes.grids.hex.HexGrid
import io.github.ocirne.mazes.grids.polar.PolarGrid
import io.github.ocirne.mazes.grids.triangle.TriangleGrid
import io.github.ocirne.mazes.grids.upsilon.UpsilonGrid
import org.junit.jupiter.api.Test

class WilsonsIsPerfectTest {

    private val size = 10

    @Test
    fun `Cartesian Grid with Wilsons maze is perfect`() {
        val grid = CartesianGrid(size, size)
        val maze = Wilsons().on(grid)

        CycleDetection(maze).assertNoCycle()
    }

    @Test
    fun `Polar Grid with Wilsons maze is perfect`() {
        val grid = PolarGrid(size)
        val maze = Wilsons().on(grid)

        CycleDetection(maze).assertNoCycle()
    }

    @Test
    fun `Triangle Grid with Wilsons maze is perfect`() {
        val grid = TriangleGrid(size, size)
        val maze = Wilsons().on(grid)

        CycleDetection(maze).assertNoCycle()
    }

    @Test
    fun `Hex Grid with Wilsons maze is perfect`() {
        val grid = HexGrid(size, size)
        val maze = Wilsons().on(grid)

        CycleDetection(maze).assertNoCycle()
    }

    @Test
    fun `Upsilon Grid with Wilsons maze is perfect`() {
        val grid = UpsilonGrid(size, size)
        val maze = Wilsons().on(grid)

        CycleDetection(maze).assertNoCycle()
    }
}