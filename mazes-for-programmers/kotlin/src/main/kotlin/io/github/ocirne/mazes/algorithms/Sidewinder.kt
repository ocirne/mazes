package io.github.ocirne.mazes.algorithms

import io.github.ocirne.mazes.grids.CartesianCell
import io.github.ocirne.mazes.grids.CartesianGrid
import io.github.ocirne.mazes.grids.Cell
import io.github.ocirne.mazes.grids.MutableGrid
import kotlin.random.Random.Default.nextBoolean

class Sidewinder : PassageCarver {

    override fun on(grid: MutableGrid, startAt: Cell) {
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
    }
}
