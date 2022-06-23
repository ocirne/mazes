package io.github.ocirne.mazes.grids

import kotlin.math.PI
import kotlin.math.roundToInt

class PolarWeaveGrid(val rows: Int): PolarGrid(rows) {

    val underCells: MutableList<PolarCell> = mutableListOf()

    init {
        grid = prepareGrid2()
        configureCells()
    }

    fun prepareGrid2(): Array<Array<PolarCell>> {
        val rings = mutableListOf<Array<PolarCell>>()

        val rowHeight = 1.0 / rows.toFloat()
        rings.add(arrayOf(PolarCell(0, 0)))

        for (row in 1..rows) {
            val radius = row.toFloat() / rows
            val circumference = 2 * PI * radius

            val previousCount = rings[row - 1].size
            val estimatedCellWidth = circumference.toFloat() / previousCount
            val ratio = (estimatedCellWidth / rowHeight).roundToInt()

            val cells = previousCount * ratio
            rings.add(Array(cells) { col -> PolarOverCell(row, col, this) })
        }
        return Array(rows) { row -> rings[row] }
    }

    fun tunnelUnder(overCell: PolarOverCell) {
        val underCell = PolarUnderCell(overCell)
        underCells.add(underCell)
    }

    override fun eachCell(): List<PolarCell> {
        if (underCells == null) {
            return super.eachCell()
        }
        return super.eachCell() + underCells
    }

    override fun size(): Int {
        return super.size() + underCells.size
    }
}
