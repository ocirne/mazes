import math

from PIL import Image, ImageColor, ImageDraw

from grid import Grid
from image_saver import save
from upsilon_cell import UpsilonCell
from recursive_backtracker import RecursiveBacktracker


class UpsilonGrid(Grid):
    def __init__(self, rows, columns):
        self.distances = None
        self.maximum = None
        super().__init__(rows, columns)

    def prepare_grid(self):
        return [[UpsilonCell(row, column) for column in range(self.columns)] for row in range(self.rows)]

    def configure_cells(self):
        for cell in self.each_cell():
            row, col = cell.row, cell.column
            cell.north = self[row - 1, col]
            cell.east = self[row, col + 1]
            cell.south = self[row + 1, col]
            cell.west = self[row, col - 1]
            if cell.is_octogon():
                cell.northwest = self[row - 1, col - 1]
                cell.northeast = self[row - 1, col + 1]
                cell.southeast = self[row + 1, col + 1]
                cell.southwest = self[row + 1, col - 1]

    def to_img(self, cell_size=10, wall_size=3, inset=0.0):
        if inset != 0.0:
            raise NotImplementedError
        c_size = cell_size
        half_c_size = c_size / 2
        a_size = cell_size / math.sqrt(2)
        corrected_size = c_size + a_size

        img_width = int(corrected_size * (self.columns + 1))
        img_height = int(corrected_size * (self.rows + 1))

        background = ImageColor.getrgb("black")
        wall = ImageColor.getrgb("white")

        img = Image.new("RGB", (img_width + 1, img_height + 1), background)
        draw = ImageDraw.Draw(img)

        for mode in ["backgrounds", "walls"]:
            for cell in self.each_cell():
                cx = (cell.column + 1) * corrected_size
                cy = (cell.row + 1) * corrected_size

                x0 = cx - half_c_size - a_size
                x1 = cx - half_c_size
                x2 = cx + half_c_size
                x3 = cx + half_c_size + a_size

                y0 = cy - half_c_size - a_size
                y1 = cy - half_c_size
                y2 = cy + half_c_size
                y3 = cy + half_c_size + a_size

                if cell.is_octogon():
                    p0 = x0, y2
                    p1 = x0, y1
                    p2 = x1, y0
                    p3 = x2, y0
                    p4 = x3, y1
                    p5 = x3, y2
                    p6 = x2, y3
                    p7 = x1, y3
                else:
                    p0 = p7 = x1, y2
                    p2 = p1 = x1, y1
                    p4 = p3 = x2, y1
                    p6 = p5 = x2, y2

                if mode == "backgrounds":
                    color = self.background_color_for(cell)
                    if color is not None:
                        if cell.is_octogon():
                            points = (p0, p1, p2, p3, p4, p5, p6, p7)
                        else:
                            points = (p0, p2, p4, p6)
                        draw.polygon(points, color, color)
                else:
                    if not cell.west:
                        draw.line((p0, p1), wall, wall_size)
                    if not cell.north:
                        draw.line((p2, p3), wall, wall_size)
                    if not cell.is_linked(cell.east):
                        draw.line((p4, p5), wall, wall_size)
                    if not cell.is_linked(cell.south):
                        draw.line((p6, p7), wall, wall_size)

                    if cell.is_octogon():
                        if not cell.northwest:
                            draw.line((p1, p2), wall, wall_size)
                        if not cell.is_linked(cell.northeast):
                            draw.line((p3, p4), wall, wall_size)
                        if not cell.is_linked(cell.southeast):
                            draw.line((p5, p6), wall, wall_size)
                        if not cell.southwest:
                            draw.line((p7, p0), wall, wall_size)

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


if __name__ == "__main__":
    grid = UpsilonGrid(10, 10)
    RecursiveBacktracker.on(grid)

    start = grid[0, 0]
    distances = start.distances()
    grid.set_distances(distances)

    save(grid.to_img(), filename="upsilon.png")
