package io.github.ocirne.mazes.grids

interface GridProvider {

    fun createPassageCarver(): MutableGrid

    fun createWallAdder(): MutableGrid
}