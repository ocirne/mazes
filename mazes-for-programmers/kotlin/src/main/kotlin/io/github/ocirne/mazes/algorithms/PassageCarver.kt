package io.github.ocirne.mazes.algorithms

import io.github.ocirne.mazes.grids.Cell
import io.github.ocirne.mazes.grids.GridProvider
import io.github.ocirne.mazes.grids.MutableGrid


interface PassageCarver {

    fun on(gridProvider: GridProvider, startAt: (MutableGrid) -> Cell={ it.randomCell() }): MutableGrid
}
