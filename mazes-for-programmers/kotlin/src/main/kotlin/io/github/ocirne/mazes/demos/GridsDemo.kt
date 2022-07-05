package io.github.ocirne.mazes.demos

import io.github.ocirne.mazes.grids.*
import io.github.ocirne.mazes.output.save

fun main() {
    CartesianGrid(10, 10).toImage().save("cartesian")
    PolarGrid(5).toImage().save("polar")
    TriangleGrid(8, 13).toImage().save("triangle")
    HexGrid(9, 11).toImage().save("hex")
    UpsilonGrid(9, 9).toImage().save("upsilon")
}
