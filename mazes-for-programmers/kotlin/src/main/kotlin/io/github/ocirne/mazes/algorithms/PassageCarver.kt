package io.github.ocirne.mazes.algorithms

import io.github.ocirne.mazes.grids.CartesianGrid


interface PassageCarver {

    fun on(grid: CartesianGrid)
}