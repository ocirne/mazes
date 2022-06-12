from mask import Mask
from masked_grid import MaskedGrid
from recursive_backtracker import RecursiveBacktracker
from image_saver import save

if __name__ == "__main__":
    mask = Mask.from_txt("mask.txt")
    grid = MaskedGrid(mask)
    RecursiveBacktracker.on(grid)

    save(grid.to_img(), filename="masked_grid.png")
