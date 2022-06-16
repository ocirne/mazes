package io.github.ocirne.mazes.grids

class PolarCell(val row: Int, val column: Int) : Cell() {

    var cw: PolarCell? = null
    var ccw: PolarCell? = null
    var inward: PolarCell? = null
    val outward: MutableList<PolarCell> = mutableListOf()

    override fun neighbors(): List<PolarCell> {
        return arrayListOf(cw, ccw, inward).filterNotNull() + outward
    }
}
