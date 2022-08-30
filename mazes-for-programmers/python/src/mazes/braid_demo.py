from colored_grid import ColoredGrid
from recursive_backtracker import RecursiveBacktracker
from image_saver import save


def braid_demo():
    grid = ColoredGrid(11, 11)
    RecursiveBacktracker.on(grid)
    save(grid.to_img(), "non_braided.png")

    start = grid[5, 5]
    grid.set_distances(start.distances())
    save(grid.to_img(), "non_braided_colored.png")

    grid.reset_distances()
    grid.braid()
    save(grid.to_img(), "braided.png")

    start = grid[5, 5]
    grid.set_distances(start.distances())
    save(grid.to_img(), "braided_colored.png")


if __name__ == "__main__":
    braid_demo()
