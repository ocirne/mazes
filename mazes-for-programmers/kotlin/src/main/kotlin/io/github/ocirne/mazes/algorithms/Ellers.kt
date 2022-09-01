package io.github.ocirne.mazes.algorithms

import io.github.ocirne.mazes.grids.Cell
import io.github.ocirne.mazes.grids.GridProvider
import io.github.ocirne.mazes.grids.Maze
import io.github.ocirne.mazes.grids.cartesian.CartesianGrid.CartesianCell
import io.github.ocirne.mazes.grids.cartesian.CartesianGrid.CartesianMaze
import kotlin.random.Random.Default.nextInt

class Ellers : PassageCarver {

    override fun on(gridProvider: GridProvider, startAt: (Maze) -> Cell): Maze {
        val maze = gridProvider.forPassageCarver() as CartesianMaze
        var rowState = RowState()
        for (row in maze.eachRow()) {
            for (cell in row) {
                if (cell.west == null) {
                    continue
                }
                val currentSet = rowState.setFor(cell)
                val priorSet = rowState.setFor(cell.west as CartesianCell)
                val shouldLink = currentSet != priorSet && (cell.south == null || nextInt(2) == 0)
                if (shouldLink) {
                    cell.link(cell.west as CartesianCell)
                    rowState.merge(priorSet, currentSet)
                }
            }
            if (row[0].south != null) {
                val nextRow = rowState.next()
                for (cells in rowState.eachSet()) {
                    cells.shuffled().forEachIndexed { index, cell ->
                        if (index == 0 || nextInt(3) == 0) {
                            cell.link(cell.south as CartesianCell)
                            nextRow.record(rowState.setFor(cell), cell.south as CartesianCell)
                        }
                    }
                }
                rowState = nextRow
            }
        }
        return maze
    }

    internal class RowState(startingSet: Int = 0) {

        private val cellsInSet = mutableMapOf<Int, MutableSet<CartesianCell>>()
        private val setForCell = mutableMapOf<Int, Int>()
        private var nextSet = startingSet

        fun record(set: Int, cell: CartesianCell) {
            setForCell[cell.column] = set
            if (!cellsInSet.contains(set)) {
                cellsInSet[set] = mutableSetOf()
            }
            cellsInSet[set]!!.add(cell)
        }

        fun setFor(cell: CartesianCell): Int {
            if (!setForCell.contains(cell.column)) {
                record(nextSet, cell)
                nextSet++
            }
            return setForCell[cell.column]!!
        }

        fun merge(winner: Int, loser: Int) {
            for (cell in cellsInSet[loser]!!) {
                setForCell[cell.column] = winner
                cellsInSet[winner]!!.add(cell)
            }
            cellsInSet.remove(loser)
        }

        fun next(): RowState {
            return RowState(nextSet)
        }

        fun eachSet(): MutableCollection<MutableSet<CartesianCell>> {
            return cellsInSet.values
        }
    }
}
