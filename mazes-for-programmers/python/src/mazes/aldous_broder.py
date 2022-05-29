from grid import Grid
from random import choice


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


# Demo
if __name__ == "__main__":
    grid = Grid(20, 20)
    AldousBroder.on(grid)
    grid.to_img()
