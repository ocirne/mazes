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
        throw NotImplementedError("Please use specialized functions")
    }

    private fun generalizedEllers() {
        TODO()
    }

    fun onCartesianGrid(grid: GridProvider): Maze {
        val maze = grid.forPassageCarver() as CartesianMaze
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

    fun onPolarGrid(grid: GridProvider, fromCenter: Boolean): Maze {
        val maze = grid.forPassageCarver() as PolarGrid.PolarMaze
        return if (fromCenter) onPolarFromCenter(maze) else onPolarFromEdge(maze)
    }

    private fun onPolarFromEdge(maze: PolarGrid.PolarMaze): Maze {
        var rowState = RowState()
        for (row in maze.eachRow(reversed = true)) {
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

    private fun onPolarFromCenter(maze: PolarGrid.PolarMaze): Maze {
        var rowState = RowState()
        for (row in maze.eachRow()) {
            for (cell in row) {
                val currentSet = rowState.setFor(cell)
                if (cell.cw == null) {
                    continue
                }
                val priorSet = rowState.setFor(cell.cw as PolarCell)
                val shouldLink = currentSet != priorSet && (cell.outward.isEmpty() || nextInt(2) == 0)
                if (shouldLink) {
                    cell.link(cell.cw as PolarCell)
                    rowState.merge(priorSet, currentSet)
                }
            }
            if (row[0].outward.isNotEmpty()) {
                val nextRow = rowState.next()
                for (cells in rowState.eachSet()) {
                    cells.shuffled().forEachIndexed { index, cell ->
                        cell as PolarCell
                        val currentSet = rowState.getSetForCell(cell)!!
                        for (nextCell in cell.outward) {
                            if (index == 0 || nextInt(3) == 0) {
                                if (nextCell != null && !nextRow.containsSet(currentSet)) {
                                    cell.link(nextCell as PolarCell)
                                    nextRow.record(rowState.setFor(cell), nextCell as PolarCell)
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
