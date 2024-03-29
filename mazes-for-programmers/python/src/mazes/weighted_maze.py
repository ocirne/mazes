from random import choice

from recursive_backtracker import RecursiveBacktracker
from image_saver import save
from weighted_grid import WeightedGrid


def weighted_maze_demo():
    grid = WeightedGrid(11, 11)
    RecursiveBacktracker.on(grid)

    grid.braid(0.5)
    start, finish = grid[0, 0], grid[grid.rows - 1, grid.columns - 1]

    grid.set_distances(start.distances().path_to(finish))
    save(grid.to_img(), "weighted_original.png")

    lava = choice(list(grid.distances.cells.keys()))
    lava.weight = 50
    grid.set_distances(start.distances().path_to(finish))

    save(grid.to_img(), "weighted_rerouted.png")


if __name__ == "__main__":
    weighted_maze_demo()
