package io.github.ocirne.mazes.grids

import java.awt.Graphics

class TriangleCell(val row: Int, val column: Int) : Cell() {

    var east: TriangleCell? = null
    var west: TriangleCell? = null
    var north: TriangleCell? = null
    var south: TriangleCell? = null

    override fun neighbors(): List<TriangleCell> {
        return arrayListOf(west, east, if (isUpright()) south else north).filterNotNull()
    }

    fun isUpright(): Boolean {
        return (row + column) % 2 == 0
    }

    override fun drawBackground(g: Graphics) {
        TODO("Not yet implemented")
    }

    override fun drawWalls(g: Graphics) {
        TODO("Not yet implemented")
    }
}
