from cell import Cell, wall_size
from random import choice, randint, sample, random
from PIL import Image, ImageDraw, ImageColor


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
    SPRITE = {
        (0, 0, 0, 0): " ",
        # u250x
        (0, 0, 1, 1): "\u2500",
        (0, 0, 2, 2): "\u2501",
        (1, 1, 0, 0): "\u2502",
        (2, 2, 0, 0): "\u2503",
        # ...
        (0, 1, 0, 1): "\u250C",
        (0, 1, 0, 2): "\u250D",
        (0, 2, 0, 1): "\u250E",
        (0, 2, 0, 2): "\u250F",
        # u251x
        (0, 1, 1, 0): "\u2510",
        (0, 1, 2, 0): "\u2511",
        (0, 2, 1, 0): "\u2512",
        (0, 2, 2, 0): "\u2513",
        (1, 0, 0, 1): "\u2514",
        (1, 0, 0, 2): "\u2515",
        (2, 0, 0, 1): "\u2516",
        (2, 0, 0, 2): "\u2517",
        (1, 0, 1, 0): "\u2518",
        (1, 0, 2, 0): "\u2519",
        (2, 0, 1, 0): "\u251A",
        (2, 0, 2, 0): "\u251B",
        (1, 1, 0, 1): "\u251C",
        (1, 1, 0, 2): "\u251D",
        (2, 1, 0, 1): "\u251E",
        (1, 2, 0, 1): "\u251F",
        # u252x
        (2, 2, 0, 1): "\u2520",
        (2, 1, 0, 2): "\u2521",
        (1, 2, 0, 2): "\u2522",
        (2, 2, 0, 2): "\u2523",
        (1, 1, 1, 0): "\u2524",
        (1, 1, 2, 0): "\u2525",
        (2, 1, 1, 0): "\u2526",
        (1, 2, 1, 0): "\u2527",
        (2, 2, 1, 0): "\u2528",
        (2, 1, 2, 0): "\u2529",
        (1, 2, 2, 0): "\u252A",
        (2, 2, 2, 0): "\u252B",
        (0, 1, 1, 1): "\u252C",
        (0, 1, 2, 1): "\u252D",
        (0, 1, 1, 2): "\u252E",
        (0, 1, 2, 2): "\u252F",
        # u253x
        (0, 2, 1, 1): "\u2530",
        (0, 2, 2, 1): "\u2531",
        (0, 2, 1, 2): "\u2532",
        (0, 2, 2, 2): "\u2533",
        (1, 0, 1, 1): "\u2534",
        (1, 0, 2, 1): "\u2535",
        (1, 0, 1, 2): "\u2536",
        (1, 0, 2, 2): "\u2537",
        (2, 0, 1, 1): "\u2538",
        (2, 0, 2, 1): "\u2539",
        (2, 0, 1, 2): "\u253A",
        (2, 0, 2, 2): "\u253B",
        (1, 1, 1, 1): "\u253C",
        (1, 1, 2, 1): "\u253D",
        (1, 1, 1, 2): "\u253E",
        (1, 1, 2, 2): "\u253F",
        # u254x
        (2, 1, 1, 1): "\u2540",
        (1, 2, 1, 1): "\u2541",
        (2, 2, 1, 1): "\u2542",
        (2, 1, 2, 1): "\u2543",
        (2, 1, 1, 2): "\u2544",
        (1, 2, 2, 1): "\u2545",
        (1, 2, 1, 2): "\u2546",
        (2, 1, 2, 2): "\u2547",
        (1, 2, 2, 2): "\u2548",
        (2, 2, 2, 1): "\u2549",
        (2, 2, 1, 2): "\u254A",
        (2, 2, 2, 2): "\u254B",
        # u257x
        (0, 0, 1, 0): "\u2574",
        (1, 0, 0, 0): "\u2575",
        (0, 0, 0, 1): "\u2576",
        (0, 1, 0, 0): "\u2577",
        (0, 0, 2, 0): "\u2578",
        (2, 0, 0, 0): "\u2579",
        (0, 0, 0, 2): "\u257A",
        (0, 2, 0, 0): "\u257B",
        (0, 0, 1, 2): "\u257C",
        (1, 2, 0, 0): "\u257D",
        (0, 0, 2, 1): "\u257E",
        (2, 1, 0, 0): "\u257F",
    }

    def sprite(self, n, s, w, e):
        if (n, s, w, e) in self.SPRITE:
            return self.SPRITE[(n, s, w, e)]
        print("missing:", (n, s, w, e))
        raise

    def horizontal_wall(self, cell1, cell2):
        """
        --
        """
        v = wall_size(cell1, cell2)
        return self.sprite(0, 0, v, v) * 3

    def vertical_wall(self, cell1, cell2):
        """
        |
        """
        v = wall_size(cell1, cell2)
        return self.sprite(v, v, 0, 0)

    def corner(self, cell1, cell2, cell3, cell4):
        """
        1 2
        3 4
        """
        n = wall_size(cell1, cell2)
        s = wall_size(cell3, cell4)
        w = wall_size(cell1, cell3)
        e = wall_size(cell2, cell4)
        return self.sprite(n, s, w, e)

    def to_str_unicode(self):
        output = ""
        # top left corner
        output += self.corner(None, None, None, self[0, 0])

        # top boundary: for every cell top boundary and top right corner
        for x in range(self.columns):
            top_boundary = self.horizontal_wall(None, self[x, 0])
            top_right_corner = self.corner(None, None, self[x, 0], self[x + 1, 0])
            output += top_boundary + top_right_corner
        output += "\n"

        for y in range(self.rows):
            # first cell handles the left wall and bottom left corner
            top = self.vertical_wall(None, self[0, y])
            bottom_left_corner = self.corner(None, self[0, y], None, self[0, y + 1])
            bottom = bottom_left_corner

            # every cell handle right wall and bottom right corner
            for x in range(self.columns):
                body = self.contents_of(self[x, y])
                east_boundary = self.vertical_wall(self[x, y], self[x + 1, y])
                top += body + east_boundary

                south_boundary = self.horizontal_wall(self[x, y], self[x, y + 1])
                bottom_right_corner = self.corner(self[x, y], self[x + 1, y], self[x, y + 1], self[x + 1, y + 1])
                bottom += south_boundary + bottom_right_corner
            output += top + "\n" + bottom + "\n"

        return output

    def background_color_for(self, cell):
        return None

    def to_img(self, cell_size=100, wall_size=3, inset=0.0):
        img_width = cell_size * self.columns
        img_height = cell_size * self.rows
        inset = int(cell_size * inset)

        background = ImageColor.getrgb("black")
        wall_color = ImageColor.getrgb("white")

        img = Image.new("RGB", (img_width + 1, img_height + 1), background)
        draw = ImageDraw.Draw(img)

        for mode in ["backgrounds", "walls"]:
            for cell in self.each_cell():
                x = cell.column * cell_size
                y = cell.row * cell_size
                floor_color = self.background_color_for(cell)
                if inset > 0:
                    self.to_img_with_inset(draw, cell, mode, cell_size, wall_color, floor_color, wall_size, x, y, inset)
                else:
                    self.to_img_without_inset(draw, cell, mode, cell_size, wall_color, floor_color, wall_size, x, y)

        return img

    def to_img_without_inset(self, draw, cell, mode, cell_size, wall_color, floor_color, wall_size, x1, y1):
        x2 = x1 + cell_size
        y2 = y1 + cell_size

        if mode == "backgrounds":
            if floor_color is not None:
                draw.rectangle((x1, y1, x2, y2), floor_color, floor_color)
        else:
            if not cell.north:
                draw.line((x1, y1, x2, y1), wall_color, wall_size)
            if not cell.west:
                draw.line((x1, y1, x1, y2), wall_color, wall_size)
            if not cell.is_linked(cell.east):
                draw.line((x2, y1, x2, y2), wall_color, wall_size)
            if not cell.is_linked(cell.south):
                draw.line((x1, y2, x2, y2), wall_color, wall_size)

    def cell_coordinates_with_inset(self, x, y, cell_size, inset):
        x1, x4 = x, x + cell_size
        x2 = x1 + inset
        x3 = x4 - inset

        y1, y4 = y, y + cell_size
        y2 = y1 + inset
        y3 = y4 - inset

        return x1, x2, x3, x4, y1, y2, y3, y4

    def to_img_with_inset(self, draw, cell, mode, cell_size, wall_color, floor_color, wall_size, x, y, inset):
        x1, x2, x3, x4, y1, y2, y3, y4 = self.cell_coordinates_with_inset(x, y, cell_size, inset)
        if mode == "backgrounds":
            if floor_color is not None:
                draw.rectangle((x2, y2, x3, y3), floor_color, floor_color)
                if cell.is_linked(cell.north):
                    draw.rectangle((x2, y1, x3, y2), floor_color, floor_color)
                if cell.is_linked(cell.south):
                    draw.rectangle((x2, y3, x3, y4), floor_color, floor_color)
                if cell.is_linked(cell.west):
                    draw.rectangle((x1, y2, x2, y3), floor_color, floor_color)
                if cell.is_linked(cell.east):
                    draw.rectangle((x3, y2, x4, y3), floor_color, floor_color)
        else:
            if cell.is_linked(cell.north):
                draw.line((x2, y1, x2, y2), wall_color, wall_size)
                draw.line((x3, y1, x3, y2), wall_color, wall_size)
            else:
                draw.line((x2, y2, x3, y2), wall_color, wall_size)

            if cell.is_linked(cell.south):
                draw.line((x2, y3, x2, y4), wall_color, wall_size)
                draw.line((x3, y3, x3, y4), wall_color, wall_size)
            else:
                draw.line((x2, y3, x3, y3), wall_color, wall_size)

            if cell.is_linked(cell.west):
                draw.line((x1, y2, x2, y2), wall_color, wall_size)
                draw.line((x1, y3, x2, y3), wall_color, wall_size)
            else:
                draw.line((x2, y2, x2, y3), wall_color, wall_size)

            if cell.is_linked(cell.east):
                draw.line((x3, y2, x4, y2), wall_color, wall_size)
                draw.line((x3, y3, x4, y3), wall_color, wall_size)
            else:
                draw.line((x3, y2, x3, y3), wall_color, wall_size)

    def deadends(self):
        return [cell for cell in self.each_cell() if len(cell.links) == 1]

    def braid(self, p=1.0):
        deadends = self.deadends()
        for cell in sample(deadends, len(deadends)):
            if len(cell.links) != 1 or random() > p:
                continue
            neighbors = [n for n in cell.neighbors() if not cell.is_linked(n)]
            best = [n for n in neighbors if len(n.links) == 1]
            if not best:
                best = neighbors

            neighbor = choice(best)
            cell.link(neighbor)
