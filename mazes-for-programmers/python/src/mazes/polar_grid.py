from datetime import datetime
from math import cos, sin, pi
from random import randint

from PIL import ImageColor, ImageDraw, Image

from grid import Grid
from polar_cell import PolarCell
from recursive_backtracker import RecursiveBacktracker


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

    def to_img(self, cell_size=10, wall_size=3, filename=None, lfd=0, extension="png", save=True):
        img_size = 2 * self.rows * cell_size

        background = ImageColor.getrgb("white")
        wall = ImageColor.getrgb("black")

        img = Image.new("RGB", (img_size + 1, img_size + 1), background)
        draw = ImageDraw.Draw(img)

        center = img_size // 2

        for mode in ["backgrounds", "walls"]:
            for cell in self.each_cell():
                if cell.row == 0:
                    continue
                theta = 2 * pi / len(self.grid[cell.row])
                inner_radius = cell.row * cell_size
                outer_radius = (cell.row + 1) * cell_size
                theta_ccw = cell.column * theta
                theta_cw = (cell.column + 1) * theta

                ax = center + (inner_radius * cos(theta_ccw))
                ay = center + (inner_radius * sin(theta_ccw))
                bx = center + (outer_radius * cos(theta_ccw))
                by = center + (outer_radius * sin(theta_ccw))

                cx = center + (inner_radius * cos(theta_cw))
                cy = center + (inner_radius * sin(theta_cw))
                dx = center + (outer_radius * cos(theta_cw))
                dy = center + (outer_radius * sin(theta_cw))

                if mode == "backgrounds":
                    color = self.background_color_for(cell)
                    if color is not None:
                        draw.polygon((ax, ay, bx, by, dx, dy, cx, cy), fill=color, outline=color)
                else:
                    if not cell.is_linked(cell.inward):
                        draw.line((ax, ay, cx, cy), fill=wall, width=wall_size)
                    if not cell.is_linked(cell.cw):
                        draw.line((cx, cy, dx, dy), fill=wall, width=wall_size)
        draw.ellipse((0, 0, img_size, img_size), width=wall_size, outline=wall)

        if save:
            if filename is None:
                filename = datetime.now().strftime("%%Y-%%m-%%d-%%H%%M%%S-%s.%s" % (lfd, extension))
            print("write to file", filename)
            img.save("images/" + filename)
        else:
            return img

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
    grid = PolarGrid(20)
    RecursiveBacktracker.on(grid)

    start = grid[0, 0]
    distances = start.distances()
    grid.set_distances(distances)

    grid.to_img()
