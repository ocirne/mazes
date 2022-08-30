from random import choice

from image_saver import save
from colored_grid import ColoredGrid


class Wilsons:
    @staticmethod
    def on(grid):
        unvisited = list(grid.each_cell())
        first = choice(unvisited)
        unvisited.remove(first)
        while unvisited:
            cell = choice(unvisited)
            path = [cell]
            while cell in unvisited:
                cell = choice(cell.neighbors())
                position = path.index(cell) if cell in path else None
                if position is not None:
                    path = path[0 : position + 1]
                else:
                    path.append(cell)
            for index in range(len(path) - 1):
                path[index].link(path[index + 1])
                unvisited.remove(path[index])
        return grid


def wilsons_demo():
    grid = ColoredGrid(11, 11)
    Wilsons.on(grid)
    save(grid.to_img(), "wilsons.png")

    middle = grid[grid.rows // 2, grid.columns // 2]
    grid.set_distances(middle.distances())

    save(grid.to_img(), "wilsons_colored.png")


if __name__ == "__main__":
    wilsons_demo()
