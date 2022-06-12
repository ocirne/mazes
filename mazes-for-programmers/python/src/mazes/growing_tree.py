from random import choice, randint

from colored_grid import ColoredGrid


class GrowingTree:
    @staticmethod
    def on(grid, select_cell, start_at=None):
        if start_at is None:
            start_at = grid.random_cell()
        active = [start_at]
        while active:
            cell = select_cell(active)
            available_neighbors = [n for n in cell.neighbors() if not n.links]
            if available_neighbors:
                neighbor = choice(available_neighbors)
                cell.link(neighbor)
                active.append(neighbor)
            else:
                active.remove(cell)
        return grid


def create_growing_tree_grid(desc, select_cell):
    grid = ColoredGrid(21, 21)
    start = grid[10, 10]
    GrowingTree.on(grid, select_cell, start)
    grid.to_img(filename="growing_tree_%s.png" % desc)

    grid.set_distances(start.distances())
    grid.to_img(filename="growing_tree_%s_colored.png" % desc)


# Demo
if __name__ == "__main__":
    create_growing_tree_grid("random", lambda cells: choice(cells))
    create_growing_tree_grid("last", lambda cells: cells[-1])
    create_growing_tree_grid("mix", lambda cells: cells[-1] if randint(0, 1) == 0 else choice(cells))
