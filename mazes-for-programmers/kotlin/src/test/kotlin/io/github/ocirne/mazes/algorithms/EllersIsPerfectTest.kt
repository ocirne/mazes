package io.github.ocirne.mazes.algorithms

import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.grids.cartesian.CartesianGrid
import io.github.ocirne.mazes.grids.polar.PolarGrid
import io.github.ocirne.mazes.output.save
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

        val colorization = Colorization(maze).dijkstra(maze[0, 0]!!)
        maze.toImage(baseSize = 100.0, backgroundColors = colorization).save("polar_ellers_colored")

        CycleDetection(maze).assertNoCycle()
    }
}