from random import randint, seed

from mask import Mask
from masked_grid import MaskedGrid
from recursive_backtracker import RecursiveBacktracker
from image_saver import save


def ascii_mask_demo():
    mask = Mask.from_txt("mask.txt")
    grid = MaskedGrid(mask)
    RecursiveBacktracker.on(grid)
    save(grid.to_img(), "masked_ascii_grid.png")

    middle = grid[grid.rows // 2, grid.columns // 2]
    grid.set_distances(middle.distances())
    save(grid.to_img(), "masked_ascii_grid_colored.png")


def random_ascii_mask_demo():
    """
    >>> seed(1)
    >>> maze = random_ascii_mask_demo()
    >>> print(maze)
    +---+---+---+---+---+
    |       |   |   |   |
    +---+   +---+---+   +
    |   |       |       |
    +---+---+   +   +   +
    |   |   |       |   |
    +---+---+---+---+   +
    |   |       |   |   |
    +---+   +   +---+   +
    |       |           |
    +---+---+---+---+---+
    <BLANKLINE>
    >>> print(maze.to_str_unicode())
    ┏━━━━━━━┓       ┏━━━┓
    ┃       ┃       ┃   ┃
    ┗━━━┓   ┗━━━┯━━━┛   ┃
        ┃       │       ┃
        ┗━━━┓   ╵   ╷   ┃
            ┃       │   ┃
        ┏━━━┹───┲━━━┪   ┃
        ┃       ┃   ┃   ┃
    ┏━━━┛   ╷   ┗━━━┛   ┃
    ┃       │           ┃
    ┗━━━━━━━┷━━━━━━━━━━━┛
    <BLANKLINE>
    """
    mask = Mask(5, 5)
    for i in range(10):
        mask[randint(0, 3), randint(0, 3)] = False

    grid = MaskedGrid(mask)
    RecursiveBacktracker.on(grid)

    return grid


if __name__ == "__main__":
    ascii_mask_demo()
    print(random_ascii_mask_demo().to_str_unicode())
