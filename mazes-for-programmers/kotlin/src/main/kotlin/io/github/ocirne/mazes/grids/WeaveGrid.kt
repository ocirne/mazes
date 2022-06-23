package io.github.ocirne.mazes.grids

class WeaveGrid(val rows: Int, val columns: Int): CartesianGrid(rows, columns) {

    val underCells: MutableList<CartesianCell> = mutableListOf()

    init {
        grid = prepareGrid2()
        configureCells()
    }

    fun prepareGrid2(): Array<Array<CartesianCell>> {
        return Array(rows) { row -> Array(columns) { column -> OverCell(row, column, this) } }
    }

    fun tunnelUnder(overCell: OverCell) {
        val underCell = UnderCell(overCell)
        underCells.add(underCell)
    }

    override fun eachCell(): List<CartesianCell> {
        if (underCells == null) {
            return super.eachCell()
        }
        return super.eachCell() + underCells
    }

    override fun size(): Int {
        return super.size() + underCells.size
    }
}
