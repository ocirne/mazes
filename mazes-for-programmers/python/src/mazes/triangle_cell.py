from cell import Cell


class TriangleCell(Cell):
    def is_upright(self):
        return (self.row + self.column) % 2 == 0

    def neighbors(self):
        result = []
        if self.west is not None:
            result.append(self.west)
        if self.east is not None:
            result.append(self.east)
        if (not self.is_upright()) and (self.north is not None):
            result.append(self.north)
        if (self.is_upright()) and (self.south is not None):
            result.append(self.south)
        return result
