from cell import Cell
from random import randint


class Grid:
    def __init__(self, rows: int, columns: int):
        self.rows = rows
        self.columns = columns
        self.grid = self.prepare_grid()
        self.configure_cells()

    def prepare_grid(self):
        return [
            Cell(row, column)
            for row in range(self.rows)
            for column in range(self.columns)
        ]

    def configure_cells(self):
        for cell in self.each_cell():
            row, col = cell.row, cell.column
            cell.north = self.grid[row - 1, col]
            cell.south = self.grid[row + 1, col]
            cell.west = self.grid[row, col - 1]
            cell.east = self.grid[row, col + 1]

    def __getitem__(self, row, column):
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
