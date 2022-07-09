package io.github.ocirne.mazes.pages

import io.github.ocirne.mazes.grids.*
import io.github.ocirne.mazes.output.formatForPages
import io.github.ocirne.mazes.output.save

fun main() {
    val baseSize = 40.0
    CartesianGrid(10, 10).toImage(baseSize).formatForPages().save("cartesian")
    PolarGrid(5).toImage(baseSize).formatForPages().save("polar")
    TriangleGrid(8, 13).toImage(baseSize).formatForPages().save("triangle")
    HexGrid(9, 11).toImage(baseSize).formatForPages().save("hex")
    UpsilonGrid(9, 9).toImage(baseSize).formatForPages().save("upsilon")
}
