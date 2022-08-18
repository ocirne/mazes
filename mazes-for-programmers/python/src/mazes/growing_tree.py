from random import choice, randint

from colored_grid import ColoredGrid
from image_saver import save


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
    grid = ColoredGrid(11, 11)
    middle = grid[5, 5]
    GrowingTree.on(grid, select_cell, start_at=middle)
    save(grid.to_img(cell_size=20), filename="growing_tree_%s.png" % desc)

    grid.set_distances(middle.distances())
    save(grid.to_img(cell_size=20), filename="growing_tree_%s_colored.png" % desc)


def growing_tree_demo():
    create_growing_tree_grid("random", lambda cells: choice(cells))
    create_growing_tree_grid("last", lambda cells: cells[-1])
    create_growing_tree_grid("mix", lambda cells: cells[-1] if randint(0, 1) == 0 else choice(cells))


if __name__ == "__main__":
    growing_tree_demo()
