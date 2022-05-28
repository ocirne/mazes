from colored_grid import ColoredGrid
from aldous_broder import AldousBroder

for n in range(6):
    grid = ColoredGrid(20, 20)
    AldousBroder.on(grid)

    middle = grid[grid.rows // 2, grid.columns // 2]
    grid.set_distances(middle.distances())

    grid.to_img(lfd=n)
