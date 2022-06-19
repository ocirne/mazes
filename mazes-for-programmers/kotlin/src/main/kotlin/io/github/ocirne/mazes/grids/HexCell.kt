package io.github.ocirne.mazes.grids

import java.awt.Graphics

class HexCell(val row: Int, val column: Int) : Cell() {

    var north: HexCell? = null
    var northeast: HexCell? = null
    var southeast: HexCell? = null
    var south: HexCell? = null
    var southwest: HexCell? = null
    var northwest: HexCell? = null

    override fun neighbors(): List<HexCell> {
        return arrayListOf(north, northeast, southeast, south, southwest, northwest).filterNotNull()
    }

    override fun drawBackground(g: Graphics) {
        TODO("Not yet implemented")
    }

    override fun drawWalls(g: Graphics) {
        TODO("Not yet implemented")
    }
}
