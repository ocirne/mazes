from random import choice

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


# Demo
if __name__ == "__main__":
    grid = ColoredGrid(20, 20)
    start = grid.random_cell()
    SimplifiedPrims.on(grid, start)

    grid.to_img(filename="prims_simple.png")

    grid.set_distances(start.distances())

    grid.to_img(filename="prims_simple_colored.png")
