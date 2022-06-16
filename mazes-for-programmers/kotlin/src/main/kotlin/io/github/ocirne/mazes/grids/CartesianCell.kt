package io.github.ocirne.mazes.grids

class CartesianCell(val row: Int, val column: Int) : Cell() {

    var north: CartesianCell? = null
    var south: CartesianCell? = null
    var east: CartesianCell? = null
    var west: CartesianCell? = null

    override fun neighbors(): List<CartesianCell> {
        return arrayListOf(north, south, east, west).filterNotNull()
    }
}