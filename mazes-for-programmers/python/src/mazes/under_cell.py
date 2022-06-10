from cell import Cell


class UnderCell(Cell):
    def __init__(self, over_cell):
        super().__init__(over_cell.row, over_cell.column)

        if over_cell.horizontal_passage():
            self.north = over_cell.north
            over_cell.north.south = self
            self.south = over_cell.south
            over_cell.south.north = self
            self.link(self.north)
            self.link(self.south)
        else:
            self.east = over_cell.east
            over_cell.east.west = self
            self.west = over_cell.west
            over_cell.west.east = self
            self.link(self.east)
            self.link(self.west)

    def horizontal_passage(self):
        return self.east or self.west

    def vertical_passage(self):
        return self.north or self.south
