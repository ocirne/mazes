package io.github.ocirne.mazes

class PolarCell(override val row: Int, override val column: Int) : Cell(row, column) {

    var cw: Cell? = null
    var ccw: Cell? = null
    var inward: Cell? = null
    val outward: MutableList<Cell> = mutableListOf()

    override fun neighbors(): List<Cell?> {
        return arrayListOf(cw, ccw, inward).filterNotNull() + outward
    }
}
