from random import choice
from grid import Grid
from image_saver import save
from colored_grid import ColoredGrid


class RecursiveBacktracker:
    @staticmethod
    def on(grid: Grid, start_at=None):
        while start_at is None:
            start_at = grid.random_cell()
        stack = [start_at]
        while stack:
            current = stack[-1]
            neighbors = [n for n in current.neighbors() if not n.links]
            if not neighbors:
                stack.pop()
            else:
                neighbor = choice(neighbors)
                current.link(neighbor)
                stack.append(neighbor)
        return grid


def recursive_backtracker_demo():
    grid = ColoredGrid(11, 11)
    RecursiveBacktracker.on(grid)
    save(grid.to_img(cell_size=20), "recursive_backtracker.png")

    middle = grid[grid.rows // 2, grid.columns // 2]
    grid.set_distances(middle.distances())

    save(grid.to_img(cell_size=20), "recursive_backtracker_colored.png")


# Demo
if __name__ == "__main__":
    recursive_backtracker_demo()
