package io.github.ocirne.mazes.algorithms

import io.github.ocirne.mazes.grids.Cell
import io.github.ocirne.mazes.grids.GridProvider
import io.github.ocirne.mazes.grids.Maze
import io.github.ocirne.mazes.grids.cartesian.CartesianGrid.CartesianCell
import io.github.ocirne.mazes.grids.polar.PolarGrid.PolarCell
import kotlin.random.Random.Default.nextInt

class Ellers : PassageCarver {

    override fun on(gridProvider: GridProvider, startAt: (Maze) -> Cell): Maze {
        throw NotImplementedError("Please use specialized functions")
    }

    private fun <C: Cell> generalizedEllers(grid: GridProvider,
                                  reversedRows: Boolean,
                                  left: (C) -> C?,
                                  down: (C) -> List<C>): Maze {
        val maze = grid.forPassageCarver()
        var rowState = RowState()
        for (row in maze.eachRow(reversedRows)) {
            for (cell in row) {
                cell as C
                val currentSet = rowState.setFor(cell)
                val leftCell = left(cell) ?: continue
                val priorSet = rowState.setFor(leftCell)
                val shouldLink = currentSet != priorSet && (down(cell).isEmpty() || nextInt(2) == 0)
                if (shouldLink) {
                    cell.link(leftCell)
                    rowState.merge(priorSet, currentSet)
                }
            }
            if (down(row[0] as C).isNotEmpty()) {
                val nextRow = rowState.next()
                for (cells in rowState.eachSet()) {
                    cells.shuffled().forEachIndexed { index, cell ->
                        cell as C
                        val currentSet = rowState.getSetForCell(cell)!!
                        for (nextCell in down(cell)) {
                            if (index == 0 || nextInt(3) == 0) {
                                if (!nextRow.containsSet(currentSet)) {
                                    cell.link(nextCell)
                                    nextRow.record(rowState.setFor(cell), nextCell)
                                }
                            }
                        }
                    }
                }
                rowState = nextRow
            }
        }
        return maze
    }

    fun onCartesianGrid(grid: GridProvider): Maze {
        return generalizedEllers<CartesianCell>(grid, false,
            left = { cell -> cell.west },
            down = { cell -> listOfNotNull(cell.south) })
    }

    fun onPolarGridFromCenter(grid: GridProvider): Maze {
        return generalizedEllers<PolarCell>(grid, false,
            left = { cell -> cell.cw },
            down = { cell -> cell.outward })
    }

    fun onPolarGridFromEdge(grid: GridProvider): Maze {
        return generalizedEllers<PolarCell>(grid, true,
            left = { cell -> cell.cw },
            down = { cell -> listOfNotNull(cell.inward) })
    }

    internal class RowState(startingSet: Int = 0) {

        private val cellsInSet = mutableMapOf<Int, MutableSet<Cell>>()
        private val setForCell = mutableMapOf<Cell, Int>()
        private var nextSet = startingSet

        fun containsSet(set: Int): Boolean {
            return cellsInSet.contains(set)
        }

        fun getSetForCell(cell: Cell): Int? {
            return setForCell[cell]
        }

        fun record(set: Int, cell: Cell) {
            setForCell[cell] = set
            if (!cellsInSet.contains(set)) {
                cellsInSet[set] = mutableSetOf()
            }
            cellsInSet[set]!!.add(cell)
        }

        fun setFor(cell: Cell): Int {
            if (!setForCell.contains(cell)) {
                record(nextSet, cell)
                nextSet++
            }
            return setForCell[cell]!!
        }

        fun merge(winner: Int, loser: Int) {
            for (cell in cellsInSet[loser]!!) {
                setForCell[cell] = winner
                cellsInSet[winner]!!.add(cell)
            }
            cellsInSet.remove(loser)
        }

        fun next(): RowState {
            return RowState(nextSet)
        }

        fun eachSet(): MutableCollection<MutableSet<Cell>> {
            return cellsInSet.values
        }
    }
}
