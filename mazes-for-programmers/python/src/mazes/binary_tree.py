from random import choice
from grid import Grid


class BinaryTree:
    @staticmethod
    def on(grid):
        for cell in grid.each_cell():
            neighbors = []
            if cell.north:
                neighbors.append(cell.north)
            if cell.east:
                neighbors.append(cell.east)
            if neighbors:
                neighbor = choice(neighbors)
                cell.link(neighbor)
        return grid


if __name__ == "__main__":
    grid = Grid(4, 4)
    BinaryTree().on(grid)
    print(grid)

    grid.to_img()
