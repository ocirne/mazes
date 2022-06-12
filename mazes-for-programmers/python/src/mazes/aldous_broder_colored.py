from colored_grid import ColoredGrid
from aldous_broder import AldousBroder
from image_saver import save

if __name__ == "__main__":
    for n in range(6):
        grid = ColoredGrid(20, 20)
        AldousBroder.on(grid)

        middle = grid[grid.rows // 2, grid.columns // 2]
        grid.set_distances(middle.distances())

        save(grid.to_img(), lfd=n)
