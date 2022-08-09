from random import seed

from distance_grid import DistanceGrid
from binary_tree import BinaryTree


def longest_path_demo():
    """
    >>> seed(42)
    >>> print(longest_path_demo())
    +---+---+---+---+---+
    |      8   7   6   5|
    +   +   +   +   +   +
    |   |  9|   |   |  4|
    +   +   +   +   +   +
    |   | 10|   |   |  3|
    +   +   +---+   +   +
    |   | 11|       |  2|
    +---+   +   +---+   +
    | 13  12|   |  0   1|
    +---+---+---+---+---+
    <BLANKLINE>
    """
    grid = DistanceGrid(5, 5)
    BinaryTree.on(grid)

    start_helper = grid[0, 0]
    distances = start_helper.distances()
    start, _ = distances.max()

    new_distances = start.distances()
    goal, _ = new_distances.max()

    grid.distances = new_distances.path_to(goal)
    return grid


if __name__ == "__main__":
    print(longest_path_demo())
