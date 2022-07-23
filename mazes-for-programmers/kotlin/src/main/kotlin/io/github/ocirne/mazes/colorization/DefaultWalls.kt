package io.github.ocirne.mazes.colorization

import io.github.ocirne.mazes.grids.Cell
import io.github.ocirne.mazes.grids.MutableGrid
import java.awt.Color

class DefaultWalls(grid: MutableGrid): Colorization(grid) {

    override fun valueFor(cell: Cell): Color {
        return Color(33, 33, 33)
    }
}