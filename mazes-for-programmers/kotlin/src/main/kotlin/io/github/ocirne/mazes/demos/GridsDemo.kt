package io.github.ocirne.mazes.demos

import io.github.ocirne.mazes.grids.*
import io.github.ocirne.mazes.output.save

fun main() {
    CartesianGrid(10, 10).toImage().save("cartesian")
    PolarGrid(5).toImage().save("polar")
    TriangleGrid(7, 10).toImage(cellSize=40).save("triangle")
    HexGrid(6, 7).toImage().save("hex")
    UpsilonGrid(7, 7).toImage(cellSize=15).save("upsilon")
}
