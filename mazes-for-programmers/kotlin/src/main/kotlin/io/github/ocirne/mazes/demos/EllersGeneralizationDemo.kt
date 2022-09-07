package io.github.ocirne.mazes.demos

import io.github.ocirne.mazes.algorithms.Ellers
import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.grids.hex.HexGrid
import io.github.ocirne.mazes.grids.polar.PolarGrid
import io.github.ocirne.mazes.grids.triangle.TriangleGrid
import io.github.ocirne.mazes.grids.upsilon.UpsilonGrid
import io.github.ocirne.mazes.output.save

fun polarGridWithEllersDemo() {
    val grid = PolarGrid(6)
    val mazeFromEdge = Ellers().onPolarGridFromEdge(grid)
    mazeFromEdge.toImage().save("polar_ellers_from_edge")

    val colorizationFromEdge = Colorization(mazeFromEdge).dijkstra(mazeFromEdge[0, 0]!!)
    mazeFromEdge.toImage(backgroundColors = colorizationFromEdge).save("polar_ellers_from_edge_colored")

    val mazeFromCenter = Ellers().onPolarGridFromCenter(grid)
    mazeFromCenter.toImage().save("polar_ellers_from_center")

    val colorizationFromCenter = Colorization(mazeFromCenter).dijkstra(mazeFromCenter[0, 0]!!)
    mazeFromCenter.toImage(backgroundColors = colorizationFromCenter).save("polar_ellers_from_center_colored")
}

fun hexGridWithEllersDemo() {
    TODO("Mazes are not perfect")
    val grid = HexGrid(10, 11)
    val maze = Ellers().onHexGrid(grid)
    maze.toImage().save("hex_ellers")

    val colorization = Colorization(maze).dijkstra(maze[5, 5]!!)
    maze.toImage(backgroundColors = colorization).save("hex_ellers_colored")
}

fun triangleGridWithEllersDemo() {
    TODO("Mazes are not perfect")
    val grid = TriangleGrid(8, 13)
    val maze = Ellers().onTriangleGrid(grid)
    maze.toImage().save("triangle_ellers")

    val colorization = Colorization(maze).dijkstra(maze[4, 6]!!)
    maze.toImage(backgroundColors = colorization).save("triangle_ellers_colored")
}

fun upsilonGridWithEllersDemo() {
    val grid = UpsilonGrid(11, 11)
    val maze = Ellers().onUpsilonGrid(grid)
    maze.toImage().save("upsilon_ellers")

    val colorization = Colorization(maze).dijkstra(maze[5, 5]!!)
    maze.toImage(backgroundColors = colorization).save("upsilon_ellers_colored")
}

fun main() {
    polarGridWithEllersDemo()
    hexGridWithEllersDemo()
    triangleGridWithEllersDemo()
    upsilonGridWithEllersDemo()
}
