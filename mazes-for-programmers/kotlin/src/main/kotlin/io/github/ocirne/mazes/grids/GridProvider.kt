package io.github.ocirne.mazes.grids

interface GridProvider {

    fun forPassageCarver(): Maze

    fun forWallAdder(): Maze
}