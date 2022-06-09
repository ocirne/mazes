from colored_grid import ColoredGrid
from recursive_backtracker import RecursiveBacktracker

if __name__ == "__main__":
    grid = ColoredGrid(21, 21)
    RecursiveBacktracker.on(grid)
    grid.to_img(inset=0.1, filename="recursive_backtracker_inset_plain.png")

    start = grid[10, 10]
    grid.set_distances(start.distances())

    grid.to_img(inset=0.1, filename="recursive_backtracker_inset_colored.png")
