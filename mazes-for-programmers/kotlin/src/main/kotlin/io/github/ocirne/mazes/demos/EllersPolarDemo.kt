package io.github.ocirne.mazes.demos

import io.github.ocirne.mazes.algorithms.Ellers
import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.grids.polar.PolarGrid
import io.github.ocirne.mazes.output.save

fun main() {
    val grid = PolarGrid(6)
    val mazeFromEdge = Ellers().onPolarGrid(grid, fromCenter = false)
    mazeFromEdge.toImage().save("polar_ellers_from_edge")

    val colorizationFromEdge = Colorization(mazeFromEdge).dijkstra(mazeFromEdge[0, 0]!!)
    mazeFromEdge.toImage(backgroundColors = colorizationFromEdge).save("polar_ellers_from_edge_colorized")

    val mazeFromCenter = Ellers().onPolarGrid(grid, fromCenter = true)
    mazeFromCenter.toImage().save("polar_ellers_from_center")

    val colorizationFromCenter = Colorization(mazeFromCenter).dijkstra(mazeFromCenter[0, 0]!!)
    mazeFromCenter.toImage(backgroundColors = colorizationFromCenter).save("polar_ellers_from_center_colorized")
}