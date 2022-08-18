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


def binary_tree_ascii_demo():
    """
    >>> seed(42)
    >>> print(binary_tree_ascii_demo())
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
    return BinaryTree().on(Grid(4, 4))


def binary_tree_image_demo():
    maze = BinaryTree().on(Grid(4, 4))
    save(maze.to_img(), "binary_tree.png")


def binary_tree_unicode_demo():
    """
    >>> seed(42)
    >>> print(binary_tree_unicode_demo())
    ┏━━━━━━━━━━━━━━━┓
    ┃               ┃
    ┃   ╷   ╷   ╷   ┃
    ┃   │   │   │   ┃
    ┃   ├───┘   │   ┃
    ┃   │       │   ┃
    ┃   │   ╷   │   ┃
    ┃   │   │   │   ┃
    ┗━━━┷━━━┷━━━┷━━━┛
    <BLANKLINE>
    """
    return BinaryTree().on(Grid(4, 4)).to_str_unicode()


if __name__ == "__main__":
    print(binary_tree_ascii_demo())
    print(binary_tree_unicode_demo())
    binary_tree_image_demo()
