package io.github.ocirne.mazes.demos

import io.github.ocirne.mazes.grids.*
import io.github.ocirne.mazes.output.save

fun main() {
    CartesianGrid(11, 11).toImage().save("cartesian")
    PolarGrid(11).toImage().save("polar")
    TriangleGrid(11, 18).toImage().save("triangle")
    HexGrid(10, 10).toImage().save("hex")
    UpsilonGrid(11, 11).toImage().save("upsilon")
}
