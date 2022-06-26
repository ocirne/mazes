package io.github.ocirne.mazes.colorization

import io.github.ocirne.mazes.grids.Cell
import io.github.ocirne.mazes.grids.Grid
import java.awt.Color

class DefaultBackground(grid: Grid): Colorization(grid) {

    override fun valueFor(cell: Cell): Color {
        return Color(33, 33, 33)
    }
}
