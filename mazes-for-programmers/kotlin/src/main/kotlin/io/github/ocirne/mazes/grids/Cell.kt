package io.github.ocirne.mazes.grids

import io.github.ocirne.mazes.colorization.Colorization
import java.awt.Graphics2D

abstract class Cell {

    private val links: MutableMap<Cell, Boolean> = mutableMapOf()

    open fun link(cell: Cell, bidi: Boolean = true) {
        if (!neighbors().contains(cell)) {
            throw IllegalArgumentException("linked cell must be a neighbor of this cell")
        }
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

    abstract fun drawBackground(g: Graphics2D, colorization: Colorization)

    abstract fun drawWalls(g: Graphics2D, colorization: Colorization)
}