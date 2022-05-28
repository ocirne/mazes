from colored_grid import ColoredGrid
from binary_tree import BinaryTree

grid = ColoredGrid(25, 25)
BinaryTree.on(grid)

start = grid[grid.rows // 2, grid.columns // 2]

grid.set_distances(start.distances())

grid.to_img(cell_size=20)
