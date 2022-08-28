from mask import Mask
from masked_grid import MaskedGrid
from recursive_backtracker import RecursiveBacktracker
from image_saver import save


def image_mask_demo():
    mask = Mask.from_png("mask.png")
    grid = MaskedGrid(mask)
    RecursiveBacktracker.on(grid)
    save(grid.to_img(cell_size=11), "masked_image_grid.png")

    middle = grid[19, 9]
    grid.set_distances(middle.distances())
    save(grid.to_img(cell_size=11), "masked_image_grid_colored.png")


if __name__ == "__main__":
    image_mask_demo()
