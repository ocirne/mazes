from cell import Cell
from random import randint
from PIL import Image, ImageDraw, ImageColor
from datetime import datetime


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
        row = randint(0, self.rows - 1)
        column = randint(0, len(self.grid[row]) - 1)
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

    def contents_of(self, cell):
        return "   "

    def __str__(self):
        output = "+" + "---+" * self.columns + "\n"
        for row in self.each_row():
            top = "|"
            bottom = "+"
            for cell in row:
                if not cell:
                    cell = Cell(-1, -1)
                body = self.contents_of(cell)
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
        (2, 0, 0, 0): "\u2579",
        (0, 2, 0, 0): "\u257B",
        (0, 0, 2, 0): "\u2578",
        (0, 0, 0, 2): "\u257A",
        (0, 2, 1, 2): "\u2532",
        (1, 2, 0, 2): "\u2522",
        (2, 2, 1, 1): "\u2542",
        (2, 1, 2, 1): "\u2543",
        (2, 1, 1, 2): "\u2544",
        (1, 2, 2, 1): "\u2545",
        (1, 2, 1, 2): "\u2546",
        (1, 1, 2, 2): "\u253F",
        (1, 2, 0, 0): "\u257D",
        (1, 0, 2, 0): "\u2519",
        (1, 0, 0, 2): "\u2515",
        (2, 1, 0, 0): "\u257F",
        (0, 1, 2, 0): "\u2511",
        (0, 1, 0, 2): "\u250D",
        (2, 0, 1, 0): "\u251A",
        (0, 2, 1, 0): "\u2512",
        (0, 0, 1, 2): "\u257C",
        (2, 0, 0, 1): "\u2516",
        (0, 2, 0, 1): "\u250E",
        (0, 0, 2, 1): "\u257E",
        (2, 1, 1, 0): "\u2526",
        (2, 1, 0, 1): "\u251E",
        (2, 0, 1, 1): "\u2538",
        (1, 2, 1, 0): "\u2527",
        (1, 2, 0, 1): "\u251F",
        (0, 2, 1, 1): "\u2530",
        (1, 1, 2, 0): "\u2525",
        (1, 0, 2, 1): "\u2535",
        (0, 1, 2, 1): "\u252D",
        (1, 1, 0, 2): "\u251D",
        (1, 0, 1, 2): "\u2536",
        (0, 1, 1, 2): "\u252E",
    }

    def walls(self, n, s, w, e):
        if (n, s, w, e) in self.WALLS:
            return self.WALLS[(n, s, w, e)]
        print("missing:", (n, s, w, e))
        raise

    def to_str_unicode(self):
        output = ""
        output += self.walls(0, 2, 0, 2)
        for cell in self.grid[0]:
            if not cell:
                cell = Cell(-1, -1)
            north_boundary = self.walls(0, 0, 2, 2) * 3
            s = cell.wall_size(cell.east)
            w = cell.wall_size(cell.north)
            e = cell.east.wall_size(cell.east.north) if cell.east is not None else 0
            corner = self.walls(0, s, w, e)
            output += north_boundary + corner
        output += "\n"
        for row in self.each_row():
            cell = row[0]
            if not cell:
                cell = Cell(-1, -1)
            top = self.walls(2, 2, 0, 0)
            s = cell.south.wall_size(cell.south.west) if cell.south is not None else 0
            e = cell.wall_size(cell.south)
            bottom = self.walls(2, s, 0, e)
            for cell in row:
                if not cell:
                    cell = Cell(-1, -1)
                body = self.contents_of(cell)
                e = cell.wall_size(cell.east)
                east_boundary = self.walls(e, e, 0, 0)
                top += body + east_boundary

                s = cell.wall_size(cell.south)
                south_boundary = self.walls(0, 0, s, s) * 3
                n = cell.wall_size(cell.east)
                s = cell.south.wall_size(cell.south.east) if cell.south is not None else 0
                w = cell.wall_size(cell.south)
                e = cell.east.wall_size(cell.east.south) if cell.east is not None else 0
                corner = self.walls(n, s, w, e)
                bottom += south_boundary + corner
            output += top + "\n" + bottom + "\n"

        return output

    def background_color_for(self, cell):
        return None

    def to_img(self, cell_size=100, wall_size=3, filename=None, lfd=0, extension="png", save=True):
        img_width = cell_size * self.columns
        img_height = cell_size * self.rows

        background = ImageColor.getrgb("white")
        wall = ImageColor.getrgb("black")

        img = Image.new("RGB", (img_width + 1, img_height + 1), background)
        draw = ImageDraw.Draw(img)

        for mode in ["backgrounds", "walls"]:
            for cell in self.each_cell():
                x1 = cell.column * cell_size
                y1 = cell.row * cell_size
                x2 = (cell.column + 1) * cell_size
                y2 = (cell.row + 1) * cell_size

                if mode == "backgrounds":
                    color = self.background_color_for(cell)
                    if color is not None:
                        draw.rectangle((x1, y1, x2, y2), fill=color, outline=color)
                else:
                    if not cell.north:
                        draw.line((x1, y1, x2, y1), fill=wall, width=wall_size)
                    if not cell.west:
                        draw.line((x1, y1, x1, y2), fill=wall, width=wall_size)
                    if not cell.is_linked(cell.east):
                        draw.line((x2, y1, x2, y2), fill=wall, width=wall_size)
                    if not cell.is_linked(cell.south):
                        draw.line((x1, y2, x2, y2), fill=wall, width=wall_size)

        if save:
            if filename is None:
                filename = datetime.now().strftime("images/%%Y-%%m-%%d-%%H%%M%%S-%s.%s" % (lfd, extension))
            print("write to file", filename)
            img.save(filename)
        else:
            return img

    def deadends(self):
        return [cell for cell in self.each_cell() if len(cell.links) == 1]
