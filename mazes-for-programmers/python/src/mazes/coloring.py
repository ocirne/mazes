from colored_grid import ColoredGrid
from binary_tree import BinaryTree
from sidewinder import Sidewinder
from image_saver import save


def coloring_binarytree_demo():
    grid = ColoredGrid(11, 11)
    BinaryTree.on(grid)

    start = grid[grid.rows // 2, grid.columns // 2]
    grid.set_distances(start.distances())

    save(grid.to_img(cell_size=20), filename="coloring_binarytree.png")


def coloring_sidewinder_demo():
    grid = ColoredGrid(11, 11)
    Sidewinder.on(grid)

    start = grid[grid.rows // 2, grid.columns // 2]
    grid.set_distances(start.distances())

    save(grid.to_img(cell_size=20), filename="coloring_sidewinder.png")


if __name__ == "__main__":
    coloring_binarytree_demo()
    coloring_sidewinder_demo()
