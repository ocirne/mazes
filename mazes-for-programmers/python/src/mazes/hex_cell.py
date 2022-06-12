from cell import Cell


class HexCell(Cell):
    def __init__(self, row, column):
        super().__init__(row, column)
        self.northeast = None
        self.northwest = None
        self.southeast = None
        self.southwest = None

    def neighbors(self):
        result = []
        if self.northwest is not None:
            result.append(self.northwest)
        if self.north is not None:
            result.append(self.north)
        if self.northeast is not None:
            result.append(self.northeast)
        if self.southwest is not None:
            result.append(self.southwest)
        if self.south is not None:
            result.append(self.south)
        if self.southeast is not None:
            result.append(self.southeast)
        return result
