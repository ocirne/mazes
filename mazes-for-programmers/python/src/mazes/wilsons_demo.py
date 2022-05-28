from grid import Grid
from wilsons import Wilsons

grid = Grid(20, 20)
Wilsons.on(grid)

grid.to_img()
