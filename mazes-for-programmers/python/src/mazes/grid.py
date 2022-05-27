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
        return [[Cell(row, column) for column in range(self.columns)] for row in range(self.rows)]

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

    # see https://www.compart.com/de/unicode/block/U+2500
    # TODO golf it
    WALLS = {
        (0, 0, 0, 0): " ",
        (2, 2, 2, 2): "\u254B",
        (2, 2, 0, 0): "\u2503",
        (2, 0, 2, 0): "\u251B",
        (2, 0, 0, 2): "\u2517",
        (0, 2, 2, 0): "\u2513",
        (0, 2, 0, 2): "\u250F",
        (0, 0, 2, 2): "\u2501",
        (2, 2, 1, 0): "\u2528",
        (2, 2, 0, 1): "\u2520",
        (1, 0, 2, 2): "\u2537",
        (0, 1, 2, 2): "\u252F",
        (1, 1, 1, 1): "\u253C",
        (1, 1, 1, 0): "\u2524",
        (1, 1, 0, 1): "\u251C",
        (1, 0, 1, 1): "\u2534",
        (0, 1, 1, 1): "\u252C",
        (1, 1, 0, 0): "\u2502",
        (1, 0, 1, 0): "\u2518",
        (1, 0, 0, 1): "\u2514",
        (0, 1, 1, 0): "\u2510",
        (0, 1, 0, 1): "\u250C",
        (0, 0, 1, 1): "\u2500",
        (1, 0, 0, 0): "\u2575",
        (0, 1, 0, 0): "\u2577",
        (0, 0, 1, 0): "\u2574",
        (0, 0, 0, 1): "\u2576",
    }

    def to_str_unicode(self):
        output = ""
        output += self.WALLS.get((0, 2, 0, 2))
        for cell in self.grid[0]:
            north_boundary = self.WALLS.get((0, 0, 2, 2)) * 3
            s = cell.wall_size(cell.east)
            w = cell.wall_size(cell.north)
            e = cell.east.wall_size(cell.east.north) if cell.east is not None else 0
            corner = self.WALLS.get((0, s, w, e))
            output += north_boundary + corner
        output += "\n"
        for row in self.each_row():
            cell = row[0]
            top = self.WALLS.get((2, 2, 0, 0))
            s = cell.south.wall_size(cell.south.west) if cell.south is not None else 0
            e = cell.wall_size(cell.south)
            bottom = self.WALLS.get((2, s, 0, e))
            for cell in row:
                if not cell:
                    cell = Cell(-1, -1)
                body = "   "
                e = cell.wall_size(cell.east)
                east_boundary = self.WALLS.get((e, e, 0, 0))
                top += body + east_boundary

                s = cell.wall_size(cell.south)
                south_boundary = self.WALLS.get((0, 0, s, s)) * 3
                n = cell.wall_size(cell.east)
                s = cell.south.wall_size(cell.south.east) if cell.south is not None else 0
                w = cell.wall_size(cell.south)
                e = cell.east.wall_size(cell.east.south) if cell.east is not None else 0
                corner = self.WALLS.get((n, s, w, e))
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
