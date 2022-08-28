from grid import Grid
from cell import Cell


class MaskedGrid(Grid):
    def __init__(self, mask):
        self.mask = mask
        self.distances = None
        self.maximum = None
        super().__init__(mask.rows, mask.columns)

    def prepare_grid(self):
        return [
            [Cell(row, column) if self.mask[row, column] else None for column in range(self.columns)]
            for row in range(self.rows)
        ]

    def random_cell(self):
        row, col = self.mask.random_location()
        return self[row, col]

    def size(self):
        return self.mask.count()

    def set_distances(self, distances):
        self.distances = distances
        farthest, self.maximum = distances.max()

    def background_color_for(self, cell):
        if self.distances is None or cell not in self.distances:
            return None
        distance = self.distances[cell]
        intensity = (self.maximum - distance) / self.maximum
        dark = round(255 * intensity)
        bright = 128 + round(127 * intensity)
        return dark, bright, dark
