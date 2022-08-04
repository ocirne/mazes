from random import choice, seed
from grid import Grid
from image_saver import save


class BinaryTree:
    @staticmethod
    def on(grid):
        for cell in grid.each_cell():
            neighbors = []
            if cell.north:
                neighbors.append(cell.north)
            if cell.east:
                neighbors.append(cell.east)
            if neighbors:
                neighbor = choice(neighbors)
                cell.link(neighbor)
        return grid


def binary_tree_demo():
    """
    >>> seed(42)
    >>> print(BinaryTree().on(Grid(4, 4)))
    +---+---+---+---+
    |               |
    +   +   +   +   +
    |   |   |   |   |
    +   +---+   +   +
    |   |       |   |
    +   +   +   +   +
    |   |   |   |   |
    +---+---+---+---+
    <BLANKLINE>
    """


def binary_tree_image_demo():
    maze = BinaryTree().on(Grid(4, 4))
    save(maze.to_img(), filename="binary_tree.png")


if __name__ == "__main__":
    binary_tree_image_demo()
