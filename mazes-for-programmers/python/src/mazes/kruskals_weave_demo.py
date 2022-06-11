from random import randint

from over_cell import OverCell
from kruskals import Kruskals, State
from weave_grid import WeaveGrid


class SimpleOverCell(OverCell):
    def neighbors(self):
        list = []
        if self.north is not None:
            list.append(self.north)
        if self.south is not None:
            list.append(self.south)
        if self.west is not None:
            list.append(self.west)
        if self.east is not None:
            list.append(self.east)
        return list


class PreconfiguredGrid(WeaveGrid):
    def __init__(self, rows, columns):
        super().__init__(rows, columns)
        self.distances = []

    def prepare_grid(self):
        return [[SimpleOverCell(row, column, self) for column in range(self.columns)] for row in range(self.rows)]


if __name__ == "__main__":
    grid = PreconfiguredGrid(20, 20)
    state = State(grid)

    for i in range(grid.size()):
        row = 1 + randint(0, grid.rows - 2)
        column = 1 + randint(0, grid.columns - 2)
        state.add_crossing(grid[row, column])

    Kruskals.on(grid, state)
    grid.to_img(inset=0.2, filename="kruskals_weave.png")
