package io.github.ocirne.mazes

open class Cell(open val row: Int, open val column: Int) {

    val links: MutableMap<Cell, Boolean> = mutableMapOf()

    var north: Cell? = null
    var south: Cell? = null
    var east: Cell? = null
    var west: Cell? = null

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

    open fun neighbors(): List<Cell?> {
        return arrayListOf(north, south, east, west).filterNotNull()
    }

    fun distances(): Distances {
        val distances = Distances(this)
        var frontier = mutableListOf(this)

        while (frontier.isNotEmpty()) {
            val newFrontier: MutableList<Cell> = mutableListOf()
            for (cell in frontier) {
                for (linked in cell.links()) {
                    if (distances[linked] != null) {
                        continue
                    }
                    distances[linked] = distances[cell]!! + 1
                    newFrontier.add(linked)
                }
            }
            frontier = newFrontier
        }
        return distances
    }
}