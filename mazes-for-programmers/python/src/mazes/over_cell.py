from cell import Cell


class OverCell(Cell):
    def __init__(self, row, column, grid):
        super().__init__(row, column)
        self.grid = grid

    def neighbors(self):
        result = super().neighbors()
        if self.can_tunnel_north():
            result.append(self.north.north)
        if self.can_tunnel_south():
            result.append(self.south.south)
        if self.can_tunnel_east():
            result.append(self.east.east)
        if self.can_tunnel_west():
            result.append(self.west.west)
        return result

    def can_tunnel_north(self):
        return self.north and self.north.north and self.north.horizontal_passage()

    def can_tunnel_south(self):
        return self.south and self.south.south and self.south.horizontal_passage()

    def can_tunnel_east(self):
        return self.east and self.east.east and self.east.horizontal_passage()

    def can_tunnel_west(self):
        return self.west and self.west.west and self.west.horizontal_passage()

    def horizontal_passage(self):
        return (
            self.is_linked(self.east)
            and self.is_linked(self.west)
            and not self.is_linked(self.north)
            and not self.is_linked(self.south)
        )

    def vertical_passage(self):
        return (
            self.is_linked(self.north)
            and self.is_linked(self.south)
            and not self.is_linked(self.east)
            and not self.is_linked(self.west)
        )

    def link(self, cell, bidi=True):
        neighbor = None
        if self.north and self.north == cell.south:
            neighbor = self.north
        elif self.south and self.south == cell.north:
            neighbor = self.south
        elif self.east and self.east == cell.west:
            neighbor = self.east
        elif self.west and self.west == cell.east:
            neighbor = self.west

        if neighbor is not None:
            self.grid.tunnel_under(neighbor)
        else:
            super().link(cell, bidi)
