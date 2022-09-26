package io.github.ocirne.mazes.demos

import io.github.ocirne.mazes.algorithms.Ellers
import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.grids.hex.HexGrid
import io.github.ocirne.mazes.grids.polar.PolarGrid
import io.github.ocirne.mazes.grids.triangle.TriangleGrid
import io.github.ocirne.mazes.grids.upsilon.UpsilonGrid
import io.github.ocirne.mazes.output.saveAs

fun polarGridWithEllersDemo() {
    val grid = PolarGrid(6)
    val mazeFromEdge = Ellers().onPolarGridFromEdge(grid)
    mazeFromEdge.toImage().saveAs("polar_ellers_from_edge")

    val colorizationFromEdge = Colorization(mazeFromEdge).dijkstra(mazeFromEdge[0, 0]!!)
    mazeFromEdge.toImage(backgroundColors = colorizationFromEdge).saveAs("polar_ellers_from_edge_colored")

    val mazeFromCenter = Ellers().onPolarGridFromCenter(grid)
    mazeFromCenter.toImage().saveAs("polar_ellers_from_center")

    val colorizationFromCenter = Colorization(mazeFromCenter).dijkstra(mazeFromCenter[0, 0]!!)
    mazeFromCenter.toImage(backgroundColors = colorizationFromCenter).saveAs("polar_ellers_from_center_colored")
}

fun hexGridWithEllersDemo() {
    TODO("Mazes are not perfect")
    val grid = HexGrid(10, 11)
    val maze = Ellers().onHexGrid(grid)
    maze.toImage().saveAs("hex_ellers")

    val colorization = Colorization(maze).dijkstra(maze[5, 5]!!)
    maze.toImage(backgroundColors = colorization).saveAs("hex_ellers_colored")
}

fun triangleGridWithEllersDemo() {
    TODO("Mazes are not perfect")
    val grid = TriangleGrid(8, 13)
    val maze = Ellers().onTriangleGrid(grid)
    maze.toImage().saveAs("triangle_ellers")

    val colorization = Colorization(maze).dijkstra(maze[4, 6]!!)
    maze.toImage(backgroundColors = colorization).saveAs("triangle_ellers_colored")
}

fun upsilonGridWithEllersDemo() {
    val grid = UpsilonGrid(11, 11)
    val maze = Ellers().onUpsilonGrid(grid)
    maze.toImage().saveAs("upsilon_ellers")

    val colorization = Colorization(maze).dijkstra(maze[5, 5]!!)
    maze.toImage(backgroundColors = colorization).saveAs("upsilon_ellers_colored")
}

fun main() {
    polarGridWithEllersDemo()
    hexGridWithEllersDemo()
    triangleGridWithEllersDemo()
    upsilonGridWithEllersDemo()
}
