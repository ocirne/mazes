from colored_grid import ColoredGrid
from sidewinder import Sidewinder


def create_animation(width, height):
    grid = ColoredGrid(width, height)
    Sidewinder.on(grid)

    for x in range(width):
        start = grid[0, x]
        grid.set_distances(start.distances())
        yield grid.to_img(cell_size=20, save=False)
    for x in range(width - 1, -1, -1):
        start = grid[0, x]
        grid.set_distances(start.distances())
        yield grid.to_img(cell_size=20, save=False)


it = create_animation(20, 20)
img = next(it)
img.save("images/animated_dijkstra.gif", format="GIF", append_images=it, save_all=True, duration=200, loop=0)
