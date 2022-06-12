from grid import Grid
from random import choice

from image_saver import save


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


# Demo
if __name__ == "__main__":
    grid = Grid(20, 20)
    Wilsons.on(grid)

    save(grid.to_img(), filename="wilsons.png")
