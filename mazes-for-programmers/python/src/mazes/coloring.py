from colored_grid import ColoredGrid
from binary_tree import BinaryTree
from image_saver import save

if __name__ == "__main__":
    grid = ColoredGrid(25, 25)
    BinaryTree.on(grid)

    start = grid[grid.rows // 2, grid.columns // 2]

    grid.set_distances(start.distances())

    save(grid.to_img(cell_size=20), filename="coloring.png")
