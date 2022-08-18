from random import choice
from colored_grid import ColoredGrid
from image_saver import save


class AldousBroder:
    @staticmethod
    def on(grid):
        cell = grid.random_cell()
        unvisited = grid.size() - 1
        while unvisited > 0:
            neighbor = choice(cell.neighbors())
            if not neighbor.links:
                cell.link(neighbor)
                unvisited -= 1
            cell = neighbor
        return grid


def aldous_broder_demo():
    grid = ColoredGrid(11, 11)
    AldousBroder.on(grid)
    save(grid.to_img(), "aldous_broder.png")

    middle = grid[grid.rows // 2, grid.columns // 2]
    grid.set_distances(middle.distances())

    save(grid.to_img(), "aldous_broder_colored.png")


# Demo
if __name__ == "__main__":
    aldous_broder_demo()
