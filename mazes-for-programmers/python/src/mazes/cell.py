from distances import Distances


class Cell:
    def __init__(self, row, column):
        self.row = row
        self.column = column
        self.links = {}
        self.north = None
        self.south = None
        self.east = None
        self.west = None

    def __gt__(self, other):
        return self.row < other.row

    def link(self, cell, bidi=True):
        self.links[cell] = True
        if bidi:
            cell.link(self, False)
        return self

    def unlink(self, cell, bidi=True):
        if cell in self.links:
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
        result = []
        if self.north is not None:
            result.append(self.north)
        if self.south is not None:
            result.append(self.south)
        if self.west is not None:
            result.append(self.west)
        if self.east is not None:
            result.append(self.east)
        return result

    def distances(self):
        distances = Distances(self)
        frontier = [self]
        while frontier:
            new_frontier = []
            for cell in frontier:
                for linked in cell.links:
                    if linked in distances:
                        continue
                    distances[linked] = distances[cell] + 1
                    new_frontier.append(linked)
            frontier = new_frontier
        return distances

    def __str__(self):
        return "%s %s" % (self.row, self.column)


def wall_size(cell1, cell2):
    if cell1 is None and cell2 is None:
        return 0
    if cell1 is None or cell2 is None:
        return 2
    if cell2 not in cell1.links:
        return 1
    return 0
