from random import seed

from distance_grid import DistanceGrid
from binary_tree import BinaryTree
from sidewinder import Sidewinder


def dijkstra_distances_demo():
    """
    >>> seed(23)
    >>> print(dijkstra_distances_demo())
    +---+---+---+---+---+
    |  0   1   2   3   4|
    +   +---+---+---+   +
    |  1|  8   7   6   5|
    +---+---+   +   +   +
    | 10   9   8|  7|  6|
    +   +   +---+---+   +
    | 11| 10  11  12|  7|
    +---+---+---+   +   +
    | 16  15  14  13|  8|
    +---+---+---+---+---+
    <BLANKLINE>
    """
    grid = DistanceGrid(5, 5)
    Sidewinder.on(grid)

    start = grid[0, 0]
    distances = start.distances()
    grid.set_distances(distances)

    return grid


def dijkstra_path_demo():
    """
    >>> seed(23)
    >>> print(dijkstra_path_demo())
    +---+---+---+---+---+
    |  0   1   2   3    |
    +---+---+---+   +   +
    |          5   4|   |
    +---+---+   +   +   +
    |  8   7   6|   |   |
    +   +   +   +---+   +
    |  9|   |   |       |
    +   +---+---+   +   +
    | 10|           |   |
    +---+---+---+---+---+
    <BLANKLINE>
    """
    grid = DistanceGrid(5, 5)
    BinaryTree.on(grid)

    start = grid[0, 0]
    distances = start.distances()

    grid.distances = distances.path_to(grid[grid.rows - 1, 0])
    return grid


if __name__ == "__main__":
    print(dijkstra_distances_demo())
    print(dijkstra_path_demo())
