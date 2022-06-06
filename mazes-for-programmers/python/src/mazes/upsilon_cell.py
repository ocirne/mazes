from cell import Cell


class UpsilonCell(Cell):
    def __init__(self, row, column):
        super().__init__(row, column)
        self.northeast = None
        self.northwest = None
        self.southeast = None
        self.southwest = None

    def is_octogon(self):
        return (self.row + self.column) % 2 == 0

    def neighbors(self):
        list = []
        if self.northwest is not None:
            list.append(self.northwest)
        if self.north is not None:
            list.append(self.north)
        if self.northeast is not None:
            list.append(self.northeast)
        if self.east is not None:
            list.append(self.east)
        if self.southwest is not None:
            list.append(self.southwest)
        if self.south is not None:
            list.append(self.south)
        if self.southeast is not None:
            list.append(self.southeast)
        if self.west is not None:
            list.append(self.west)
        return list
