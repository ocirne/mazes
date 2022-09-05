package io.github.ocirne.mazes.algorithms

import io.github.ocirne.mazes.grids.Cell
import io.github.ocirne.mazes.grids.GridProvider
import io.github.ocirne.mazes.grids.Maze
import io.github.ocirne.mazes.grids.cartesian.CartesianGrid.CartesianCell
import io.github.ocirne.mazes.grids.cartesian.CartesianGrid.CartesianMaze
import io.github.ocirne.mazes.grids.polar.PolarGrid
import io.github.ocirne.mazes.grids.polar.PolarGrid.PolarCell
import kotlin.random.Random.Default.nextInt

class Ellers : PassageCarver {

    override fun on(gridProvider: GridProvider, startAt: (Maze) -> Cell): Maze {
        // sieht doof aus, aber funktioniert
        return when (val grid = gridProvider.forPassageCarver()) {
            is CartesianMaze -> on(grid)
            is PolarGrid.PolarMaze -> on(grid)
            else -> throw NotImplementedError()
        }
    }

    fun on(maze: CartesianMaze): Maze {
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
                        cell as CartesianCell
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

    fun on(maze: PolarGrid.PolarMaze): Maze {
        var rowState = RowState()
        for (row in maze.eachRow()) {
            for (cell in row) {
                if (cell.cw == null) {
                    continue
                }
                val currentSet = rowState.setFor(cell)
                val priorSet = rowState.setFor(cell.cw as PolarCell)
                val shouldLink = currentSet != priorSet && (cell.inward == null || nextInt(2) == 0)
                if (shouldLink) {
                    cell.link(cell.cw as PolarCell)
                    rowState.merge(priorSet, currentSet)
                }
            }
            if (row[0].inward != null) {
                val nextRow = rowState.next()
                for (cells in rowState.eachSet()) {
                    cells.shuffled().forEachIndexed { index, cell ->
                        cell as PolarCell
                        val currentSet = rowState.getSetForCell(cell)!!
                        if (index == 0 || nextInt(3) == 0) {
                            if (cell.inward != null && !nextRow.containsSet(currentSet)) {
                                cell.link(cell.inward as PolarCell)
                                nextRow.record(rowState.setFor(cell), cell.inward as PolarCell)
                            }
                        }
                    }
                }
                rowState = nextRow
            }
        }
        return maze
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
