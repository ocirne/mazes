package io.github.ocirne.mazes.colorization

import io.github.ocirne.mazes.grids.Cell
import java.awt.Color

interface Colorization {

    fun colorForBackground(cell: Cell): Color?

    fun colorForWall(cell: Cell): Color
}