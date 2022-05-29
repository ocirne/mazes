from random import choice
from grid import Grid


class RecursiveBacktracker:
    @staticmethod
    def on(grid: Grid, start_at=None):
        if start_at is None:
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


# Demo
if __name__ == "__main__":
    grid = Grid(20, 20)
    RecursiveBacktracker.on(grid)
    grid.to_img()
