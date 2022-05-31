from grid import Grid
from cell import Cell
from mask import Mask
from recursive_backtracker import RecursiveBacktracker


class MaskedGrid(Grid):
    def __init__(self, mask):
        self.mask = mask
        super().__init__(mask.rows, mask.columns)

    def prepare_grid(self):
        return [
            [Cell(row, column) if self.mask[row, column] else None for column in range(self.columns)]
            for row in range(self.rows)
        ]

    def random_cell(self):
        row, col = self.mask.random_location()
        return self[row, col]

    def size(self):
        return self.mask.count()


# Simple mask

if __name__ == "__main__":
    mask = Mask(5, 5)
    mask[0, 0] = False
    mask[2, 2] = False
    mask[4, 4] = False

    grid = MaskedGrid(mask)
    RecursiveBacktracker.on(grid)

    print(grid)
    print(grid.to_str_unicode())
