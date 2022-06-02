from cell import Cell


class PolarCell(Cell):
    def __init__(self, row, column):
        super().__init__(row, column)
        self.cw = None
        self.ccw = None
        self.inward = None
        self.outward = []

    def neighbors(self):
        list = []
        if self.cw is not None:
            list.append(self.cw)
        if self.ccw is not None:
            list.append(self.ccw)
        if self.inward is not None:
            list.append(self.inward)
        list.extend(self.outward)
        return list
