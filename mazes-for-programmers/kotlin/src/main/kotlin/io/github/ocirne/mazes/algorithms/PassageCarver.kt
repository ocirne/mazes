package io.github.ocirne.mazes.algorithms

import io.github.ocirne.mazes.grids.Cell
import io.github.ocirne.mazes.grids.MutableGrid


interface PassageCarver {

    fun on(grid: MutableGrid, startAt: Cell = grid.randomCell())
}
