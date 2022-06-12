from math import cos, sin, pi
from random import randint

from PIL import ImageColor, ImageDraw, Image

from grid import Grid
from polar_cell import PolarCell
from recursive_backtracker import RecursiveBacktracker
from image_saver import save


class PolarGrid(Grid):
    def __init__(self, rows):
        super().__init__(rows, 1)
        self.distances = None
        self.maximum = None

    def prepare_grid(self):
        rows = []
        row_height = 1.0 / self.rows
        rows.append([PolarCell(0, 0)])
        for row in range(1, self.rows):
            radius = row / self.rows
            circumference = 2 * pi * radius
            previous_count = len(rows[-1])
            estimated_cell_width = circumference / previous_count
            ratio = round(estimated_cell_width / row_height)
            cells = previous_count * ratio
            rows.append([PolarCell(row, col) for col in range(cells)])
        return rows

    def configure_cells(self):
        for cell in self.each_cell():
            row, col = cell.row, cell.column
            if row > 0:
                cell.cw = self[row, col + 1]
                cell.ccw = self[row, col - 1]

                ratio = len(self.grid[row]) // len(self.grid[row - 1])
                parent = self.grid[row - 1][col // ratio]
                parent.outward.append(cell)
                cell.inward = parent

    def __getitem__(self, position):
        row, column = position
        if not 0 <= row < self.rows:
            return None
        return self.grid[row][column % len(self.grid[row])]

    def random_cell(self):
        row = randint(0, self.rows - 1)
        col = randint(0, len(self.grid[row]) - 1)
        return self.grid[row][col]

    def to_img(self, cell_size=10, wall_size=3, inset=None):
        img_size = 2 * self.rows * cell_size

        background = ImageColor.getrgb("black")
        wall = ImageColor.getrgb("white")

        img = Image.new("RGB", (img_size + 1, img_size + 1), background)
        draw = ImageDraw.Draw(img)

        center = img_size // 2

        for mode in ["backgrounds", "walls"]:
            for cell in self.each_cell():
                if cell.row == 0:
                    continue
                theta = 2 * pi / len(self.grid[cell.row])
                r = cell.row * cell_size
                t = cell.column * theta

                if inset is not None:
                    self.to_img_with_polar_inset(
                        draw, cell, mode, cell_size, wall, wall_size, center, r, t, theta, inset
                    )
                else:
                    self.to_img_without_polar_inset(draw, cell, mode, cell_size, wall, wall_size, center, r, t, theta)
                    draw.ellipse((0, 0, img_size, img_size), outline=wall, width=wall_size)

        return img

    def to_img_without_polar_inset(self, draw, cell, mode, cell_size, wall, wall_size, center, r, t, theta):
        r1, r4 = r, r + cell_size
        t1, t4 = t, t + theta

        ax = center + (r1 * cos(t1))
        ay = center + (r1 * sin(t1))
        bx = center + (r4 * cos(t1))
        by = center + (r4 * sin(t1))

        cx = center + (r1 * cos(t4))
        cy = center + (r1 * sin(t4))
        dx = center + (r4 * cos(t4))
        dy = center + (r4 * sin(t4))

        if mode == "backgrounds":
            color = self.background_color_for(cell)
            if color is not None:
                draw.polygon((ax, ay, bx, by, dx, dy, cx, cy), color, color)
        else:
            if not cell.is_linked(cell.inward):
                draw.line((ax, ay, cx, cy), wall, wall_size)
            if not cell.is_linked(cell.cw):
                draw.line((cx, cy, dx, dy), wall, wall_size)

    @staticmethod
    def to_polar(center, r, t):
        return center + (r * cos(t)), center + (r * sin(t))

    def cell_coordinates_with_polar_inset(self, r, t, theta, cell_size, center, inset):
        radius_inset = inset * cell_size
        r1, r4 = r, r + cell_size
        r2 = r1 + radius_inset
        r3 = r4 - radius_inset

        t1, t4 = t, t + theta

        theta_inset_1 = inset * theta * r1 / r1
        theta_inset_2 = inset * theta * r1 / r2
        theta_inset_3 = inset * theta * r1 / r3
        theta_inset_4 = inset * theta * r1 / r4

        a = self.to_polar(center, r2, t1)
        b = self.to_polar(center, r3, t1)
        c = self.to_polar(center, r1, t1 + theta_inset_1)
        d = self.to_polar(center, r2, t1 + theta_inset_2)
        e = self.to_polar(center, r3, t1 + theta_inset_3)
        f = self.to_polar(center, r4, t1 + theta_inset_4)
        g = self.to_polar(center, r1, t4 - theta_inset_1)
        h = self.to_polar(center, r2, t4 - theta_inset_2)
        i = self.to_polar(center, r3, t4 - theta_inset_3)
        k = self.to_polar(center, r4, t4 - theta_inset_4)
        m = self.to_polar(center, r2, t4)
        n = self.to_polar(center, r3, t4)

        return a, b, c, d, e, f, g, h, i, k, m, n

    # 'PolarGrid._to_img__with_polar_inset' is too complex (19)
    def to_img_with_polar_inset(  # noqa: C901
        self, draw, cell, mode, cell_size, wall, wall_size, center, r, t, theta, inset
    ):
        """
          a     b
        c d --- e f
          |     |
        g h --- i k
          m     n

        """
        a, b, c, d, e, f, g, h, i, k, m, n = self.cell_coordinates_with_polar_inset(
            r, t, theta, cell_size, center, inset
        )
        if mode == "backgrounds":
            color = self.background_color_for(cell)
            if color is not None:
                draw.polygon((d, e, i, h), color, color)
                if cell.is_linked(cell.cw):
                    draw.polygon((h, i, n, m), color, color)
                if cell.is_linked(cell.ccw):
                    draw.polygon((a, b, e, d), color, color)
                if cell.is_linked(cell.inward):
                    draw.polygon((c, d, h, g), color, color)

                if len(cell.outward) == 1 and cell.is_linked(cell.outward[0]):
                    draw.polygon((e, f, k, i), color, color)
                elif len(cell.outward) == 2:
                    sub_theta = theta / len(cell.outward)
                    for index, o in enumerate(cell.outward):
                        sub_t = t + index * sub_theta
                        a, b, c, d, e, f, g, h, i, k, m, n = self.cell_coordinates_with_polar_inset(
                            r, sub_t, theta / 2, cell_size, center, 2 * inset
                        )
                        if cell.is_linked(o):
                            draw.polygon((e, f, k, i), color, color)

        else:
            if cell.is_linked(cell.cw):
                draw.line((h, m), wall, wall_size)
                draw.line((i, n), wall, wall_size)
            else:
                draw.line((h, i), wall, wall_size)

            if cell.is_linked(cell.ccw):
                draw.line((a, d), wall, wall_size)
                draw.line((b, e), wall, wall_size)
            else:
                draw.line((d, e), wall, wall_size)

            if cell.is_linked(cell.inward):
                draw.line((c, d), wall, wall_size)
                draw.line((g, h), wall, wall_size)
            else:
                draw.line((d, h), wall, wall_size)

            if not cell.outward:
                draw.line((e, i), wall, wall_size)
            elif len(cell.outward) == 1:
                if cell.is_linked(cell.outward[0]):
                    draw.line((e, f), wall, wall_size)
                    draw.line((i, k), wall, wall_size)
                else:
                    draw.line((e, i), wall, wall_size)
            elif len(cell.outward) == 2:
                sub_theta = theta / len(cell.outward)
                for index, o in enumerate(cell.outward):
                    sub_t = t + index * sub_theta
                    a, b, c, d, e, f, g, h, i, k, m, n = self.cell_coordinates_with_polar_inset(
                        r, sub_t, theta / 2, cell_size, center, 2 * inset
                    )
                    if cell.is_linked(o):
                        draw.line((e, f), wall, wall_size)
                        draw.line((i, k), wall, wall_size)
                    else:
                        draw.line((e, i), wall, wall_size)
            else:
                raise NotImplementedError

    def set_distances(self, distances):
        self.distances = distances
        farthest, self.maximum = distances.max()

    def background_color_for(self, cell):
        if cell not in self.distances:
            return None
        distance = self.distances[cell]
        intensity = (self.maximum - distance) / self.maximum
        dark = round(255 * intensity)
        bright = 128 + round(127 * intensity)
        return dark, bright, dark


# Demo
if __name__ == "__main__":
    grid = PolarGrid(10)
    RecursiveBacktracker.on(grid)

    start = grid[0, 0]
    distances = start.distances()
    grid.set_distances(distances)

    save(grid.to_img(cell_size=80, wall_size=3), filename="polar_grid.png")
    save(grid.to_img(cell_size=80, wall_size=3, inset=0.1), filename="polar_grid_inset.png")
