package io.github.ocirne.mazes.algorithms

import io.github.ocirne.mazes.grids.CartesianGrid
import io.github.ocirne.mazes.grids.HexGrid
import io.github.ocirne.mazes.grids.PolarGrid
import io.github.ocirne.mazes.output.saveImage
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.reflect.KClass

/** from every cell all other cells are reachable in exactly one path */
internal class PerfectionTest {

    // TODO add seed feedback to reproduce errors

    private val size = 10

    companion object {
        @JvmStatic
        fun labyrinthAlgorithms(): Stream<Arguments> =
            Stream.of(
                Arguments.of(AldousBroder::class),
                Arguments.of(BinaryTree::class),
                Arguments.of(Sidewinder::class)
            )
    }

    // TODO improve
    @ParameterizedTest(name = "Cartesian grid with {0} is perfect")
    @MethodSource("labyrinthAlgorithms")
    fun `Cartesian grid with $algorithm is perfect`(labyrinthAlgorithmClass: KClass<out PassageCarver>) {
        val grid = CartesianGrid(size, size)

        val algorithm = labyrinthAlgorithmClass.constructors.first().call()
        algorithm.on(grid)

        CycleDetection(grid).assertNoCycle()
    }

    @Test
    fun `Cartesian grid with Wilsons maze is perfect`() {
        val grid = CartesianGrid(size, size)
        Wilsons.on(grid)

        saveImage(grid.toImage(), filename = "test")
        CycleDetection(grid).assertNoCycle()
    }

    @Test
    fun `Hex grid with RecursiveBacktracker maze is perfect`() {
        val grid = HexGrid(size, size)
        RecursiveBacktracker.on(grid)

        CycleDetection(grid).assertNoCycle()
    }

    @Test
    fun `Polar grid with RecursiveBacktracker maze is perfect`() {
        val grid = PolarGrid(size)
        RecursiveBacktracker.on(grid)

        CycleDetection(grid).assertNoCycle()
    }
}