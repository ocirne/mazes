from grid import Grid
from over_cell import OverCell
from recursive_backtracker import RecursiveBacktracker
from under_cell import UnderCell


class WeaveGrid(Grid):
    def __init__(self, rows, columns):
        self.under_cells = []
        super().__init__(rows, columns)

    def prepare_grid(self):
        return [[OverCell(row, column, self) for column in range(self.columns)] for row in range(self.rows)]

    def tunnel_under(self, over_cell):
        under_cell = UnderCell(over_cell)
        self.under_cells.append(under_cell)

    def each_cell(self):
        for cell in super().each_cell():
            yield cell

        for cell in self.under_cells:
            yield cell

    def to_img(self, cell_size=100, wall_size=3, inset=0.1, filename=None, lfd=0, extension="png", save=True):
        super().to_img(cell_size, wall_size, inset, filename, lfd, extension, save)

    def to_img_with_inset(self, draw, cell, mode, cell_size, wall, wall_size, x, y, inset):
        if isinstance(cell, OverCell):
            super().to_img_with_inset(draw, cell, mode, cell_size, wall, wall_size, x, y, inset)
        else:
            x1, x2, x3, x4, y1, y2, y3, y4 = self.cell_coordinates_with_inset(x, y, cell_size, inset)
            if cell.vertical_passage():
                draw.line((x2, y1, x2, y2), wall, wall_size)
                draw.line((x3, y1, x3, y2), wall, wall_size)
                draw.line((x2, y3, x2, y4), wall, wall_size)
                draw.line((x3, y3, x3, y4), wall, wall_size)
            else:
                draw.line((x1, y2, x2, y2), wall, wall_size)
                draw.line((x1, y3, x2, y3), wall, wall_size)
                draw.line((x3, y2, x4, y2), wall, wall_size)
                draw.line((x3, y3, x4, y3), wall, wall_size)


# Demo
if __name__ == "__main__":
    grid = WeaveGrid(20, 20)
    RecursiveBacktracker.on(grid)

    grid.to_img(filename="weave.png")
