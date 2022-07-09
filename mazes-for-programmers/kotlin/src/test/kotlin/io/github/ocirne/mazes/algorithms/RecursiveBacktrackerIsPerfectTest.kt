package io.github.ocirne.mazes.algorithms

import io.github.ocirne.mazes.grids.*
import org.junit.jupiter.api.Test

class RecursiveBacktrackerIsPerfectTest {

    private val size = 10

    @Test
    fun `Cartesian Grid with Recursive Backtracker maze is perfect`() {
        val grid = CartesianGrid(size, size)
        RecursiveBacktracker.on(grid)

        CycleDetection(grid).assertNoCycle()
    }

    @Test
    fun `Polar grid with RecursiveBacktracker maze is perfect`() {
        val grid = PolarGrid(size)
        RecursiveBacktracker.on(grid)

        CycleDetection(grid).assertNoCycle()
    }

    @Test
    fun `Triangle grid with RecursiveBacktracker maze is perfect`() {
        val grid = TriangleGrid(size, size)
        RecursiveBacktracker.on(grid)

        CycleDetection(grid).assertNoCycle()
    }

    @Test
    fun `Hex grid with RecursiveBacktracker maze is perfect`() {
        val grid = HexGrid(size, size)
        RecursiveBacktracker.on(grid)

        CycleDetection(grid).assertNoCycle()
    }

    @Test
    fun `Upsilon grid with RecursiveBacktracker maze is perfect`() {
        val grid = UpsilonGrid(size, size)
        RecursiveBacktracker.on(grid)

        CycleDetection(grid).assertNoCycle()
    }

    @Test
    fun `Weave grid with RecursiveBacktracker maze is perfect`() {
        val grid = WeaveGrid(size, size)
        RecursiveBacktracker.on(grid)

        CycleDetection(grid).assertNoCycle()
    }

    @Test
    fun `Polar Weave grid with RecursiveBacktracker maze is perfect`() {
        val grid = PolarWeaveGrid(size)
        RecursiveBacktracker.on(grid)

        CycleDetection(grid).assertNoCycle()
    }
}