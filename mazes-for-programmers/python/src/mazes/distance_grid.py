from grid import Grid


class DistanceGrid(Grid):
    def __init__(self, rows: int, columns: int):
        super().__init__(rows, columns)
        self.distances = None

    def set_distances(self, distances):
        self.distances = distances

    def contents_of(self, cell):
        if self.distances and self.distances[cell] is not None:
            return " %2s" % self.distances[cell]
        else:
            return super().contents_of(cell)
