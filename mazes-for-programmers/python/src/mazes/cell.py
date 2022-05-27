class Cell:
    def __init__(self, row, column):
        self.row = row
        self.column = column
        self.links = {}
        self.north = None
        self.south = None
        self.east = None
        self.west = None

    def link(self, cell, bidi=True):
        self.links[cell] = True
        if bidi:
            cell.link(self, False)
        return self

    def unlink(self, cell, bidi=True):
        del self.links[cell]
        if bidi:
            cell.unlink(cell, False)
        return self

    def links(self):
        return self.links.keys()

    def is_linked(self, cell):
        return cell in self.links

    def wall_size(self, cell):
        if cell is None:
            return 2
        if cell not in self.links:
            return 1
        return 0

    def neighbors(self):
        list = []
        if self.north is not None:
            list.append(self.north)
        if self.south is not None:
            list.append(self.south)
        if self.west is not None:
            list.append(self.west)
        if self.east is not None:
            list.append(self.east)
        return list
