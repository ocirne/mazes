from grid import Grid
from over_cell import OverCell
from recursive_backtracker import RecursiveBacktracker
from image_saver import save
from under_cell import UnderCell


class WeaveGrid(Grid):
    def __init__(self, rows, columns):
        self.under_cells = []
        self.distances = None
        self.maximum = None
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

    def to_img(self, cell_size=20, wall_size=3, inset=0.1):
        return super().to_img(cell_size, wall_size, inset)

    def to_img_with_inset(self, draw, cell, mode, cell_size, wall_color, floor_color, wall_size, x, y, inset):
        if isinstance(cell, OverCell):
            return super().to_img_with_inset(
                draw, cell, mode, cell_size, wall_color, floor_color, wall_size, x, y, inset
            )
        else:
            x1, x2, x3, x4, y1, y2, y3, y4 = self.cell_coordinates_with_inset(x, y, cell_size, inset)
            if mode == "backgrounds":
                color = self.background_color_for_under_cell(cell)
                if color is not None:
                    draw.rectangle((x2, y2, x3, y3), color, color)
                    if cell.is_linked(cell.north):
                        draw.rectangle((x2, y1, x3, y2), floor_color, floor_color)
                    if cell.is_linked(cell.south):
                        draw.rectangle((x2, y3, x3, y4), floor_color, floor_color)
                    if cell.is_linked(cell.west):
                        draw.rectangle((x1, y2, x2, y3), floor_color, floor_color)
                    if cell.is_linked(cell.east):
                        draw.rectangle((x3, y2, x4, y3), floor_color, floor_color)
            else:
                if cell.vertical_passage():
                    draw.line((x2, y1, x2, y2), wall_color, wall_size)
                    draw.line((x3, y1, x3, y2), wall_color, wall_size)
                    draw.line((x2, y3, x2, y4), wall_color, wall_size)
                    draw.line((x3, y3, x3, y4), wall_color, wall_size)
                else:
                    draw.line((x1, y2, x2, y2), wall_color, wall_size)
                    draw.line((x1, y3, x2, y3), wall_color, wall_size)
                    draw.line((x3, y2, x4, y2), wall_color, wall_size)
                    draw.line((x3, y3, x4, y3), wall_color, wall_size)

    def set_distances(self, distances):
        self.distances = distances
        farthest, self.maximum = distances.max()

    def background_color_for(self, cell):
        if self.distances is None or cell not in self.distances:
            return None
        distance = self.distances[cell]
        intensity = (self.maximum - distance) / self.maximum
        dark = round(255 * intensity)
        bright = 128 + round(127 * intensity)
        return dark, bright, dark

    def background_color_for_under_cell(self, cell):
        if cell is None:
            return None
        return self.background_color_for(cell.over_cell)


def weave_grid_demo():
    grid = WeaveGrid(11, 11)
    RecursiveBacktracker.on(grid)
    save(grid.to_img(), "weave.png")

    start = grid[0, 0]
    grid.set_distances(start.distances())
    save(grid.to_img(), "weave_colored.png")


if __name__ == "__main__":
    weave_grid_demo()
