from random import randint

from over_cell import OverCell
from kruskals import Kruskals, State
from image_saver import save
from weave_grid import WeaveGrid


class SimpleOverCell(OverCell):
    def neighbors(self):
        result = []
        if self.north is not None:
            result.append(self.north)
        if self.south is not None:
            result.append(self.south)
        if self.west is not None:
            result.append(self.west)
        if self.east is not None:
            result.append(self.east)
        return result


class PreconfiguredGrid(WeaveGrid):
    def __init__(self, rows, columns):
        super().__init__(rows, columns)
        self.distances = []
        self.maximum = None

    def prepare_grid(self):
        return [[SimpleOverCell(row, column, self) for column in range(self.columns)] for row in range(self.rows)]

    def set_distances(self, distances):
        self.distances = distances
        farthest, self.maximum = distances.max()

    def background_color_for(self, cell):
        if cell is None or cell not in self.distances:
            return None
        distance = self.distances[cell]
        intensity = (self.maximum - distance) / self.maximum
        dark = round(255 * intensity)
        bright = 128 + round(127 * intensity)
        return dark, bright, dark

    def background_color_for_under_cell(self, cell):
        if cell is None:
            return None
        return self.background_color_for(cell.over_cell)


def kruskals_weave_demo():
    grid = PreconfiguredGrid(11, 11)
    state = State(grid)

    for i in range(grid.size()):
        row = 1 + randint(0, grid.rows - 2)
        column = 1 + randint(0, grid.columns - 2)
        state.add_crossing(grid[row, column])

    Kruskals.on(grid, state)
    save(grid.to_img(inset=0.2), "kruskals_weave.png")

    start = grid[0, 0]
    grid.set_distances(start.distances())
    save(grid.to_img(inset=0.2), "kruskal_weave_colored.png")


if __name__ == "__main__":
    kruskals_weave_demo()
