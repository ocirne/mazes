package io.github.ocirne.mazes.algorithms

import io.github.ocirne.mazes.grids.*
import org.junit.jupiter.api.Test

class RecursiveBacktrackerIsPerfectTest {

    private val size = 10

    @Test
    fun `Cartesian Grid with Recursive Backtracker maze is perfect`() {
        val grid = CartesianGrid(size, size)
        val maze = RecursiveBacktracker().on(grid)

        CycleDetection(maze).assertNoCycle()
    }

    @Test
    fun `Polar grid with RecursiveBacktracker maze is perfect`() {
        val grid = PolarGrid(size)
        val maze = RecursiveBacktracker().on(grid)

        CycleDetection(maze).assertNoCycle()
    }

    @Test
    fun `Triangle grid with RecursiveBacktracker maze is perfect`() {
        val grid = TriangleGrid(size, size)
        val maze = RecursiveBacktracker().on(grid)

        CycleDetection(maze).assertNoCycle()
    }

    @Test
    fun `Hex grid with RecursiveBacktracker maze is perfect`() {
        val grid = HexGrid(size, size)
        val maze = RecursiveBacktracker().on(grid)

        CycleDetection(maze).assertNoCycle()
    }

    @Test
    fun `Upsilon grid with RecursiveBacktracker maze is perfect`() {
        val grid = UpsilonGrid(size, size)
        val maze = RecursiveBacktracker().on(grid)

        CycleDetection(maze).assertNoCycle()
    }

    @Test
    fun `Weave grid with RecursiveBacktracker maze is perfect`() {
        val grid = WeaveGrid(size, size)
        val maze = RecursiveBacktracker().on(grid)

        CycleDetection(maze).assertNoCycle()
    }

    @Test
    fun `Polar Weave grid with RecursiveBacktracker maze is perfect`() {
        val grid = PolarWeaveGrid(size)
        val maze = RecursiveBacktracker().on(grid)

        CycleDetection(maze).assertNoCycle()
    }
}