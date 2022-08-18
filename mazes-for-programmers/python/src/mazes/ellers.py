from random import randint, sample

from colored_grid import ColoredGrid
from image_saver import save


class RowState:
    def __init__(self, starting_set=0):
        self.cells_in_set = {}
        self.set_for_cell = {}
        self.next_set = starting_set

    def record(self, cell_set, cell):
        self.set_for_cell[cell.column] = cell_set
        if cell_set not in self.cells_in_set:
            self.cells_in_set[cell_set] = []
        self.cells_in_set[cell_set].append(cell)

    def set_for(self, cell):
        if cell.column not in self.set_for_cell:
            self.record(self.next_set, cell)
            self.next_set += 1
        return self.set_for_cell[cell.column]

    def merge(self, winner, loser):
        for cell in self.cells_in_set[loser]:
            self.set_for_cell[cell.column] = winner
            self.cells_in_set[winner].append(cell)
        del self.cells_in_set[loser]

    def next(self):
        return RowState(self.next_set)

    def each_set(self):
        for cell_set, cells in self.cells_in_set.items():
            yield cell_set, cells


class Ellers:
    @staticmethod
    def on(grid):
        row_state = RowState()
        for row in grid.each_row():
            for cell in row:
                if not cell.west:
                    continue
                cell_set = row_state.set_for(cell)
                prior_set = row_state.set_for(cell.west)
                should_link = cell_set != prior_set and (cell.south is None or randint(0, 1) == 0)
                if should_link:
                    cell.link(cell.west)
                    row_state.merge(prior_set, cell_set)

            if row[0].south:
                next_row = row_state.next()
                for _, cell_list in row_state.each_set():
                    for index, cell in enumerate(sample(cell_list, len(cell_list))):
                        if index == 0 or randint(0, 2) == 0:
                            cell.link(cell.south)
                            next_row.record(row_state.set_for(cell), cell.south)
                row_state = next_row


def ellers_demo():
    grid = ColoredGrid(11, 11)
    Ellers.on(grid)
    save(grid.to_img(), "ellers.png")

    middle = grid[grid.rows // 2, grid.columns // 2]
    grid.set_distances(middle.distances())

    save(grid.to_img(), "ellers_colored.png")


if __name__ == "__main__":
    ellers_demo()
