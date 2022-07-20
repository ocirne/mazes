package io.github.ocirne.mazes.demos

import io.github.ocirne.mazes.algorithms.RecursiveDivision
import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.grids.CartesianGrid
import io.github.ocirne.mazes.output.save

fun main() {
    val grid = CartesianGrid(21, 21)
    RecursiveDivision().on(grid)
    grid.toImage().save("cartesian_recursive_division")

    val colorization = Colorization(grid).dijkstra(grid[10, 10]!!)
    grid.toImage(backgroundColors = colorization).save("cartesian_recursive_division_colorized")

    val gridRooms = CartesianGrid(21, 21)
    RecursiveDivision(rooms = true).on(gridRooms)
    gridRooms.toImage().save("cartesian_recursive_division_rooms")

    // TODO buggy
    val colorizationRooms = Colorization(gridRooms).dijkstra(grid[10, 10]!!)
    gridRooms.toImage(backgroundColors = colorizationRooms).save("cartesian_recursive_division_rooms_colorized")
}
