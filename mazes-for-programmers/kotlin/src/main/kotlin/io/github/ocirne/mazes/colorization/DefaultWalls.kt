package io.github.ocirne.mazes.colorization

import io.github.ocirne.mazes.grids.Cell
import io.github.ocirne.mazes.grids.Grid
import java.awt.Color

class DefaultWalls(grid: Grid): Colorization(grid) {

    override fun valueFor(cell: Cell): Color {
        return Color.WHITE
    }
}