from random import sample

from grid import Grid


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
                self.neighbors.append([cell, cell.south])
            if cell.east:
                self.neighbors.append([cell, cell.east])

    def can_merge(self, left, right):
        return self.set_for_cell[left] != self.set_for_cell[right]

    def merge(self, left, right):
        left.link(right)
        winner = self.set_for_cell[left]
        loser = self.set_for_cell[right]
        losers = self.cells_in_set[loser] + [right]
        for cell in losers:
            self.cells_in_set[winner].append(cell)
            self.set_for_cell[cell] = winner
        del self.cells_in_set[loser]


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


# Demo
if __name__ == "__main__":
    grid = Grid(20, 20)
    Kruskals.on(grid)

    grid.to_img(filename="kruskals.png")
