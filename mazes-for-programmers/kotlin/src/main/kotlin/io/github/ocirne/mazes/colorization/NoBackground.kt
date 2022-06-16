package io.github.ocirne.mazes.colorization

import io.github.ocirne.mazes.grids.Cell
import java.awt.Color

class NoBackground : Colorization {

    override fun colorForBackground(cell: Cell): Color? {
        return null
    }

    override fun colorForWall(cell: Cell): Color {
        return Color.WHITE
    }
}