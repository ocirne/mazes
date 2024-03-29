package io.github.ocirne.mazes.demos

import io.github.ocirne.mazes.algorithms.RecursiveDivision
import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.grids.cartesian.CartesianGrid
import io.github.ocirne.mazes.output.saveAs

fun main() {
    val grid = CartesianGrid(21, 21)

    val maze = RecursiveDivision().on(grid)
    maze.toImage().saveAs("cartesian_recursive_division")

    val colorization = Colorization(maze).dijkstra(maze[10, 10]!!)
    maze.toImage(backgroundColors = colorization).saveAs("cartesian_recursive_division_colored")

    val mazeRooms = RecursiveDivision(rooms = true).on(grid)
    mazeRooms.toImage().saveAs("cartesian_recursive_division_rooms")

    // TODO buggy
    val colorizationRooms = Colorization(mazeRooms).dijkstra(maze[10, 10]!!)
    mazeRooms.toImage(backgroundColors = colorizationRooms).saveAs("cartesian_recursive_division_rooms_colored")
}
