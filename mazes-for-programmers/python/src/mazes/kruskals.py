from random import sample, randint

from image_saver import save
from colored_grid import ColoredGrid


class State:
    def __init__(self, grid):
        self.grid = grid
        self.neighbors = []
        self.set_for_cell = {}
        self.cells_in_set = {}

        for cell in self.grid.each_cell():
            already_set = len(self.set_for_cell)

            self.set_for_cell[cell] = already_set
            self.cells_in_set[already_set] = [cell]

            if cell.south:
                self.neighbors.append((cell, cell.south))
            if cell.east:
                self.neighbors.append((cell, cell.east))

    def can_merge(self, left, right):
        if left is None or right is None:
            return False
        return self.set_for_cell[left] != self.set_for_cell[right]

    def merge(self, left, right):
        left.link(right)
        winner = self.set_for_cell[left]
        loser = self.set_for_cell.get(right, None)
        if loser is not None and loser in self.cells_in_set:
            losers = self.cells_in_set[loser]
        else:
            losers = [right]
        for cell in losers:
            self.cells_in_set[winner].append(cell)
            self.set_for_cell[cell] = winner
        if loser is not None:
            del self.cells_in_set[loser]

    def add_crossing(self, cell):
        if cell.links or not self.can_merge(cell.east, cell.west) or not self.can_merge(cell.north, cell.south):
            return False
        self.neighbors[:] = [(left, right) for left, right in self.neighbors if left != cell and right != cell]

        if randint(0, 1) == 0:
            self.merge(cell.west, cell)
            self.merge(cell, cell.east)

            self.grid.tunnel_under(cell)
            self.merge(cell.north, cell.north.south)
            self.merge(cell.south, cell.south.north)
        else:
            self.merge(cell.north, cell)
            self.merge(cell, cell.south)

            self.grid.tunnel_under(cell)
            self.merge(cell.west, cell.west.east)
            self.merge(cell.east, cell.east.west)

        return True


class Kruskals:
    @staticmethod
    def on(grid, state=None):
        if state is None:
            state = State(grid)
        neighbors = sample(state.neighbors, len(state.neighbors))
        while neighbors:
            left, right = neighbors.pop()
            if state.can_merge(left, right):
                state.merge(left, right)
        return grid


def kruskals_demo():
    grid = ColoredGrid(11, 11)
    Kruskals.on(grid)
    save(grid.to_img(), "kruskals.png")

    middle = grid[5, 5]
    grid.set_distances(middle.distances())
    save(grid.to_img(), "kruskals_colored.png")


if __name__ == "__main__":
    kruskals_demo()
