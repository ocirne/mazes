package io.github.ocirne.mazes.algorithms

import io.github.ocirne.mazes.grids.CartesianGrid
import io.github.ocirne.mazes.grids.HexGrid
import io.github.ocirne.mazes.grids.PolarGrid
import io.github.ocirne.mazes.output.saveImage
import org.junit.jupiter.api.Test

/** from every cell all other cells are reachable in exactly one path */
class PerfectionTest {

    // TODO add seed feedback to reproduce errors

    private val size = 10

    @Test
    fun `Cartesian grid with BinaryTree maze is perfect`() {
        val grid = CartesianGrid(size, size)
        BinaryTree.on(grid)

        CycleDetection(grid).detect()
    }

    @Test
    fun `Cartesian grid with Sidewinder maze is perfect`() {
        val grid = CartesianGrid(size, size)
        Sidewinder.on(grid)

        CycleDetection(grid).detect()
    }

    @Test
    fun `Cartesian grid with Wilsons maze is perfect`() {
        val grid = CartesianGrid(size, size)
        Wilsons.on(grid)

        saveImage(grid.toImage(), filename = "test")
        CycleDetection(grid).detect()
    }

    @Test
    fun `Hex grid with RecursiveBacktracker maze is perfect`() {
        val grid = HexGrid(size, size)
        RecursiveBacktracker.on(grid)

        CycleDetection(grid).detect()
    }

    @Test
    fun `Polar grid with RecursiveBacktracker maze is perfect`() {
        val grid = PolarGrid(size)
        RecursiveBacktracker.on(grid)

        CycleDetection(grid).detect()
    }
}