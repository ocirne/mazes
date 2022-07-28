package io.github.ocirne.mazes.grids.cartesian

import io.github.ocirne.mazes.grids.cartesian.CartesianGrid
import io.github.ocirne.mazes.grids.Maze

class CartesianAscii(maze: CartesianGrid.CartesianMaze) {
    fun print() {
        println("hier wird der Irrgarten gedruckt ...")
    }
}


fun Maze.createAsciiArt(): CartesianAscii {
    return CartesianAscii(this as CartesianGrid.CartesianMaze)
}

