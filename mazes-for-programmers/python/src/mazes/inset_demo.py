from colored_grid import ColoredGrid
from recursive_backtracker import RecursiveBacktracker
from image_saver import save


def inset_demo():
    grid = ColoredGrid(11, 11)
    RecursiveBacktracker.on(grid)
    save(grid.to_img(inset=0.1), "insets.png")

    start = grid[5, 5]
    grid.set_distances(start.distances())
    save(grid.to_img(inset=0.1), "insets_colored.png")


if __name__ == "__main__":
    inset_demo()
