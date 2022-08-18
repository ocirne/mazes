from random import randint
from image_saver import save
from colored_grid import ColoredGrid


class RecursiveDivision:
    def __init__(self, grid, with_rooms=False):
        self.with_rooms = with_rooms
        self.grid = grid

    def on(self):
        for cell in self.grid.each_cell():
            for n in cell.neighbors():
                cell.link(n, False)
        self.divide(0, 0, self.grid.rows, self.grid.columns)

    def divide(self, row, column, height, width):
        if height <= 1 or width <= 1:
            return
        if self.with_rooms and height <= 5 and width <= 5 and randint(0, 4) == 0:
            return
        if height > width:
            self.divide_horizontally(row, column, height, width)
        else:
            self.divide_vertically(row, column, height, width)

    def divide_horizontally(self, row, column, height, width):
        divide_south_of = randint(0, height - 2)
        passage_at = randint(0, width - 1)
        for x in range(width):
            if passage_at == x:
                continue
            cell = self.grid[row + divide_south_of, column + x]
            if cell.south and cell.is_linked(cell.south):
                cell.unlink(cell.south)
        self.divide(row, column, divide_south_of + 1, width)
        self.divide(row + divide_south_of + 1, column, height - divide_south_of - 1, width)

    def divide_vertically(self, row, column, height, width):
        divide_east_of = randint(0, width - 2)
        passage_at = randint(0, height - 1)
        for y in range(height):
            if passage_at == y:
                continue
            cell = self.grid[row + y, column + divide_east_of]
            if cell.east and cell.is_linked(cell.east):
                cell.unlink(cell.east)
        self.divide(row, column, height, divide_east_of + 1)
        self.divide(row, column + divide_east_of + 1, height, width - divide_east_of - 1)


def recursive_division_demo():
    grid = ColoredGrid(11, 11)
    RecursiveDivision(grid).on()
    save(grid.to_img(), "recursive_division.png")

    middle = grid[grid.rows // 2, grid.columns // 2]
    grid.set_distances(middle.distances())

    save(grid.to_img(), "recursive_division_colored.png")


def recursive_division_rooms_demo():
    grid = ColoredGrid(11, 11)
    RecursiveDivision(grid, with_rooms=True).on()
    save(grid.to_img(), "recursive_division_rooms.png")

    middle = grid[grid.rows // 2, grid.columns // 2]
    grid.set_distances(middle.distances())

    save(grid.to_img(), "recursive_division_rooms_colored.png")


if __name__ == "__main__":
    recursive_division_demo()
    recursive_division_rooms_demo()
