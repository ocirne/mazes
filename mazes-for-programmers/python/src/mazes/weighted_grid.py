from PIL import ImageColor

from grid import Grid
from weighted_cell import WeightedCell


class WeightedGrid(Grid):
    def __init__(self, rows, columns):
        self.distances = None
        self.maximum = None
        super().__init__(rows, columns)

    def set_distances(self, distances):
        self.distances = distances
        _, self.maximum = distances.max()

    def prepare_grid(self):
        return [[WeightedCell(row, column) for column in range(self.columns)] for row in range(self.rows)]

    def background_color_for(self, cell):
        if cell.weight > 1:
            return ImageColor.getrgb("red")
        elif self.distances:
            if cell not in self.distances:
                return None
            distance = self.distances[cell]
            intensity = 64 + 191 * (self.maximum - distance) // self.maximum
            return intensity, intensity, 0
        else:
            return None
