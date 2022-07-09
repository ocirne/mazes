package io.github.ocirne.mazes.algorithms

import io.github.ocirne.mazes.grids.Grid


interface PassageCarver {

    fun on(grid: Grid)
}