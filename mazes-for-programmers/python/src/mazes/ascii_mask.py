from mask import Mask
from masked_grid import MaskedGrid
from recursive_backtracker import RecursiveBacktracker

mask = Mask.from_txt("mask.txt")
grid = MaskedGrid(mask)
RecursiveBacktracker.on(grid)

grid.to_img(filename="masked_grid.png")
