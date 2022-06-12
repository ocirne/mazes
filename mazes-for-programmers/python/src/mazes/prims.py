from heapq import heappush, heappop, heapify
from random import choice, randint

from colored_grid import ColoredGrid


class SimplifiedPrims:
    @staticmethod
    def on(grid, start_at=None):
        if start_at is None:
            start_at = grid.random_cell()
        active = [start_at]
        while active:
            cell = choice(active)
            available_neighbors = [n for n in cell.neighbors() if not n.links]
            if available_neighbors:
                neighbor = choice(available_neighbors)
                cell.link(neighbor)
                active.append(neighbor)
            else:
                active.remove(cell)
        return grid


class TruePrims:
    @staticmethod
    def on(grid, start_at=None):
        if start_at is None:
            start_at = grid.random_cell()
        costs = {}
        for cell in grid.each_cell():
            costs[cell] = randint(0, 100)
        active = []
        heappush(active, (costs[start_at], start_at))
        while active:
            cell_cost, cell = heappop(active)
            available_neighbors = [n for n in cell.neighbors() if not n.links]
            if available_neighbors:
                heappush(active, (cell_cost, cell))
                neighbor = min(available_neighbors, key=lambda n: costs[n])
                cell.link(neighbor)
                heappush(active, (costs[neighbor], neighbor))
        return grid


def simplified_prims_demo():
    grid = ColoredGrid(21, 21)
    start = grid[10, 10]
    SimplifiedPrims.on(grid, start)
    grid.to_img(filename="prims_simple.png")
    grid.set_distances(start.distances())
    grid.to_img(filename="prims_simple_colored.png")


def true_prims_demo():
    grid = ColoredGrid(21, 21)
    start = grid[10, 10]
    TruePrims.on(grid, start)
    grid.to_img(filename="prims_true.png")
    grid.set_distances(start.distances())
    grid.to_img(filename="prims_true_colored.png")


# Demo
if __name__ == "__main__":
    simplified_prims_demo()
    true_prims_demo()
