from grid import Grid
from recursive_backtracker import RecursiveBacktracker

if __name__ == "__main__":
    grid = Grid(20, 20)
    RecursiveBacktracker.on(grid)
    grid.braid(0.5)

    grid.to_img(filename="braid.png")
