from colored_grid import ColoredGrid
from recursive_backtracker import RecursiveBacktracker


if __name__ == "__main__":
    grid = ColoredGrid(21, 21)
    RecursiveBacktracker.on(grid)

    start = grid[10, 10]
    distances = start.distances()
    grid.set_distances(distances)

    grid.to_img(filename="foo.png")

    grid.braid(0.5)

    start = grid[10, 10]
    distances = start.distances()
    grid.set_distances(distances)

    grid.to_img(filename="foo_braided.png")
