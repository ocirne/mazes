from PIL import ImageColor

from grid import Grid


class ColoredGrid(Grid):
    def __init__(self, rows: int, columns: int):
        super().__init__(rows, columns)
        self.distances = None
        self.frontier = None
        self.new_frontier = None
        self.maximum = None

    def set_distances(self, distances):
        self.distances = distances
        farthest, self.maximum = self.distances.max()

    def set_frontiers(self, frontier, new_frontier):
        self.frontier = frontier
        self.new_frontier = new_frontier

    def background_color_for(self, cell):
        if self.new_frontier is not None and cell in self.new_frontier:
            return ImageColor.getrgb("red")
        elif self.frontier is not None and cell in self.frontier:
            return ImageColor.getrgb("yellow")
        elif self.distances is not None and cell in self.distances:
            distance = self.distances[cell]
            intensity = (self.maximum - distance) / self.maximum
            dark = round(255 * intensity)
            bright = 128 + round(127 * intensity)
            return dark, bright, dark
        else:
            return None
