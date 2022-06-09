from grid import Grid


class ColoredGrid(Grid):
    def __init__(self, rows: int, columns: int):
        super().__init__(rows, columns)
        self.distances = None
        self.maximum = None

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
