package io.github.ocirne.mazes.algorithms

import io.github.ocirne.mazes.grids.*
import org.junit.jupiter.api.Test

class BinaryTreeIsPerfectTest {

    private val size = 10

    @Test
    fun `Cartesian Grid with Binary Tree maze is perfect`() {
        val grid = CartesianGrid(size, size)
        BinaryTree().on(grid)

        CycleDetection(grid).assertNoCycle()
    }

    @Test
    fun `Polar Grid with Binary Tree maze is perfect`() {
        val grid = PolarGrid(size)
        BinaryTree().on(grid)

        CycleDetection(grid).assertNoCycle()
    }

    @Test
    fun `Triangle Grid with Binary Tree maze is perfect`() {
        val grid = TriangleGrid(size, size)
        BinaryTree().on(grid)

        CycleDetection(grid).assertNoCycle()
    }

    @Test
    fun `Hex Grid with Binary Tree maze is perfect`() {
        val grid = HexGrid(size, size)
        BinaryTree().on(grid)

        CycleDetection(grid).assertNoCycle()
    }

    @Test
    fun `Upsilon Grid with Binary Tree maze is perfect`() {
        val grid = UpsilonGrid(size, size)
        BinaryTree().on(grid)

        CycleDetection(grid).assertNoCycle()
    }
}