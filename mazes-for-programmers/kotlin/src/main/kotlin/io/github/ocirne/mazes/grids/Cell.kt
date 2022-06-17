package io.github.ocirne.mazes.grids

abstract class Cell {

    private val links: MutableMap<Cell, Boolean> = mutableMapOf()

    fun link(cell: Cell, bidi: Boolean = true) {
        links[cell] = true
        if (bidi)
            cell.link(this, false)
    }

    fun unlink(cell: Cell, bidi: Boolean = true) {
        links.remove(cell)
        if (bidi)
            cell.unlink(this, false)
    }

    fun links(): MutableSet<Cell> {
        return links.keys
    }

    fun isLinked(cell: Cell?): Boolean {
        return links.containsKey(cell)
    }

    abstract fun neighbors(): List<Cell>
}