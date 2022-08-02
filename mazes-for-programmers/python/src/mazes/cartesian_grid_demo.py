from grid import Grid


def cartesian_grid_demo():
    """
    >>> cartesian_grid_demo()
    +---+---+---+---+
    |   |   |   |   |
    +---+---+---+---+
    |   |   |   |   |
    +---+---+---+---+
    |   |   |   |   |
    +---+---+---+---+
    |   |   |   |   |
    +---+---+---+---+
    <BLANKLINE>
    """
    grid = Grid(4, 4)
    print(grid)


if __name__ == "__main__":
    cartesian_grid_demo()
