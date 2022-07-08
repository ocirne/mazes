package io.github.ocirne.mazes.pages

import io.github.ocirne.mazes.grids.*
import io.github.ocirne.mazes.output.formatForPages
import io.github.ocirne.mazes.output.save

fun main() {
    CartesianGrid(10, 10).toImage().formatForPages().save("cartesian")
    PolarGrid(5).toImage().formatForPages().save("polar")
    TriangleGrid(8, 13).toImage().formatForPages().save("triangle")
    HexGrid(9, 11).toImage().formatForPages().save("hex")
    UpsilonGrid(9, 9).toImage().formatForPages().save("upsilon")
}
