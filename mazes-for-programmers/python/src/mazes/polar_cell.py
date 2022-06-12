from cell import Cell


class PolarCell(Cell):
    def __init__(self, row, column):
        super().__init__(row, column)
        self.cw = None
        self.ccw = None
        self.inward = None
        self.outward = []

    def neighbors(self):
        result = []
        if self.cw is not None:
            result.append(self.cw)
        if self.ccw is not None:
            result.append(self.ccw)
        if self.inward is not None:
            result.append(self.inward)
        result.extend(self.outward)
        return result
