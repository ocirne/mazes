package io.github.ocirne.mazes.colorization

import io.github.ocirne.mazes.grids.Cell
import io.github.ocirne.mazes.grids.Maze
import java.awt.Color

class DefaultWalls(grid: Maze): Colorization(grid) {

    override fun valueFor(cell: Cell): Color {
        return Color(33, 33, 33)
    }
}