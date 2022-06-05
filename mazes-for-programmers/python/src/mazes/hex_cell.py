from cell import Cell


class HexCell(Cell):
    def __init__(self, row, column):
        super().__init__(row, column)
        self.northeast = None
        self.northwest = None
        self.southeast = None
        self.southwest = None

    def neighbors(self):
        list = []
        if self.northwest is not None:
            list.append(self.northwest)
        if self.north is not None:
            list.append(self.north)
        if self.northeast is not None:
            list.append(self.northeast)
        if self.southwest is not None:
            list.append(self.southwest)
        if self.south is not None:
            list.append(self.south)
        if self.southeast is not None:
            list.append(self.southeast)
        return list
