package io.github.ocirne.mazes.grids

import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.colorization.Strokes
import java.awt.Graphics2D

abstract class Cell {

    private val links: MutableMap<Cell, Boolean> = mutableMapOf()

    open fun link(cell: Cell, bidi: Boolean = true) {
        if (!neighbors().contains(cell)) {
            // TODO ausgesetzt, weil PolarWeave beim Verlinken die Nachbarn modifiziert
//            throw IllegalArgumentException("linked cell must be a neighbor of this cell")
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

    // TODO
    open fun drawBackground(g: Graphics2D, colorization: Colorization) {}

    open fun drawWalls(g: Graphics2D, colorization: Colorization, strokes: Strokes) {}
}