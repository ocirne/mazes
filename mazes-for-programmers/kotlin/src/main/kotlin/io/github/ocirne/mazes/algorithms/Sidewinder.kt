package io.github.ocirne.mazes.algorithms

import io.github.ocirne.mazes.grids.*
import kotlin.random.Random.Default.nextBoolean

class Sidewinder : PassageCarver {

    override fun on(gridProvider: GridProvider, startAt: (MutableGrid) -> Cell): MutableGrid {
        val grid = gridProvider.createPassageCarver()
        grid as CartesianGrid
        for (row in grid.eachRow()) {
            val run = mutableListOf<CartesianCell>()
            for (cell in row) {
                run.add(cell)
                val atEasternBoundary = cell.east == null
                val atNorthernBoundary = cell.north == null

                val shouldCloseOut = atEasternBoundary || (!atNorthernBoundary && nextBoolean())

                if (shouldCloseOut) {
                    val member = run.random()
                    if (member.north != null) {
                        member.link(member.north as CartesianCell)
                    }
                    run.clear()
                } else if (cell.east != null) {
                    cell.link(cell.east as CartesianCell)
                }
            }
        }
        return grid
    }
}
