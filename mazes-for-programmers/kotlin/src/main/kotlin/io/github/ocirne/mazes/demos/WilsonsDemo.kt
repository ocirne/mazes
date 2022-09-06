package io.github.ocirne.mazes.demos

import io.github.ocirne.mazes.algorithms.Wilsons
import io.github.ocirne.mazes.colorization.Colorization
import io.github.ocirne.mazes.grids.cartesian.CartesianGrid
import io.github.ocirne.mazes.grids.cartesian.createAsciiGrid
import io.github.ocirne.mazes.grids.cartesian.createImage
import io.github.ocirne.mazes.grids.cartesian.save

fun main() {
    val grid = CartesianGrid(11, 11)

    val maze = Wilsons().on(grid)
    println(maze.createAsciiGrid())
    maze.createImage().save("cartesian_wilsons")

    val background = Colorization(maze).dijkstra(maze[10, 5]!!)
    maze.createImage()
        .withBackgroundColors(background)
        .save("cartesian_wilsons_colored")

/*    saveImage(maze.toImage(), "cartesian_wilsons")

    val colorization1 = Colorization(maze).dijkstra(maze[10, 5]!!)
    saveImage(maze.toImage(backgroundColors = colorization1), "cartesian_wilsons_colored1")

    val colorization2 = Colorization(maze).countLinks()
    saveImage(maze.toImage(backgroundColors = colorization2), "cartesian_wilsons_colored2")
 */
}
