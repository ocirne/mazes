package io.github.ocirne.mazes.algorithms

import io.github.ocirne.mazes.grids.cartesian.CartesianGrid
import io.github.ocirne.mazes.grids.hex.HexGrid
import io.github.ocirne.mazes.grids.polar.PolarGrid
import io.github.ocirne.mazes.grids.triangle.TriangleGrid
import io.github.ocirne.mazes.grids.upsilon.UpsilonGrid
import org.junit.jupiter.api.Test

class EllersIsPerfectTest {

    private val size = 10

    @Test
    fun `Cartesian Grid with Ellers maze is perfect`() {
        val grid = CartesianGrid(size, size)
        val maze = Ellers().onCartesianGrid(grid)

        CycleDetection(maze).assertNoCycle()
    }

    @Test
    fun `Polar Grid with Ellers maze from edge is perfect`() {
        val grid = PolarGrid(size)
        val maze = Ellers().onPolarGridFromEdge(grid)

        CycleDetection(maze).assertNoCycle()
    }

    @Test
    fun `Polar Grid with Ellers maze from center is perfect`() {
        val grid = PolarGrid(size)
        val maze = Ellers().onPolarGridFromCenter(grid)

        CycleDetection(maze).assertNoCycle()
    }

    @Test
    fun `Triangle Grid with Ellers maze is perfect`() {
        val grid = TriangleGrid(size, size)
        val maze = Ellers().onTriangleGrid(grid)

        CycleDetection(maze).assertNoCycle()
    }

    @Test
    fun `Hex Grid with Ellers maze is perfect`() {
        val grid = HexGrid(size, size)
        val maze = Ellers().onHexGrid(grid)

        CycleDetection(maze).assertNoCycle()
    }

    @Test
    fun `Upsilon Grid with Ellers maze is perfect`() {
        val grid = UpsilonGrid(size, size)
        val maze = Ellers().onUpsilonGrid(grid)

        CycleDetection(maze).assertNoCycle()
    }
}