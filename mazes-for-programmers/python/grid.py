from cell import Cell
from random import randint
from PIL import Image, ImageDraw
from datetime import date, datetime


class Grid:
    def __init__(self, rows: int, columns: int):
        self.rows = rows
        self.columns = columns
        self.grid = self.prepare_grid()
        self.configure_cells()

    def prepare_grid(self):
        return [
            [Cell(row, column) for column in range(self.columns)]
            for row in range(self.rows)
        ]

    def configure_cells(self):
        for cell in self.each_cell():
            row, col = cell.row, cell.column
            cell.north = self[row - 1, col]
            cell.south = self[row + 1, col]
            cell.west = self[row, col - 1]
            cell.east = self[row, col + 1]

    def __getitem__(self, position):
        row, column = position
        if not 0 <= row < self.rows:
            return None
        if not 0 <= column < self.columns:
            return None
        return self.grid[row][column]

    def random_cell(self):
        row = randint(self.rows)
        column = randint(len(self.grid[row]))
        return self[row, column]

    def size(self):
        return self.rows * self.columns

    def each_row(self):
        for row in self.grid:
            yield row

    def each_cell(self):
        for row in self.each_row():
            for cell in row:
                if cell:
                    yield cell

    def __str__(self):
        output = "+" + "---+" * self.columns + "\n"
        for row in self.each_row():
            top = "|"
            bottom = "+"
            for cell in row:
                if not cell:
                    cell = Cell(-1, -1)
                body = "   "
                east_boundary = " " if cell.is_linked(cell.east) else "|"
                top += body + east_boundary

                south_boundary = "   " if cell.is_linked(cell.south) else "---"
                corner = "+"
                bottom += south_boundary + corner
            output += top + "\n" + bottom + "\n"

        return output

    def to_png(self, cell_size=100, wall_size=3, filename=None):
        img_width = cell_size * self.columns
        img_height = cell_size * self.rows

        # TODO PIL.ImageColor
        background = (255, 255, 255, 255)  # White
        wall = (0, 0, 0, 255)  # Black

        img = Image.new("RGBA", (img_width + 1, img_height + 1), background)
        draw = ImageDraw.Draw(img)

        for cell in self.each_cell():
            x1 = cell.column * cell_size
            y1 = cell.row * cell_size
            x2 = (cell.column + 1) * cell_size
            y2 = (cell.row + 1) * cell_size

            if not cell.north:
                draw.line((x1, y1, x2, y1), fill=wall, width=wall_size)
            if not cell.west:
                draw.line((x1, y1, x1, y2), fill=wall, width=wall_size)

            if not cell.is_linked(cell.east):
                draw.line((x2, y1, x2, y2), fill=wall, width=wall_size)
            if not cell.is_linked(cell.south):
                draw.line((x1, y2, x2, y2), fill=wall, width=wall_size)

        if filename is None:
            filename = datetime.now().strftime("images/%Y-%m-%d-%H%M%S.png")
        img.save(filename)
