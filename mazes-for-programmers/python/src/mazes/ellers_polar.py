from random import randint, sample

from polar_grid import PolarGrid
from image_saver import save

# TODO almost the same as ellers.py


class RowState:
    def __init__(self, starting_set=0):
        self.cells_in_set = {}
        self.set_for_cell = {}
        self.next_set = starting_set

    def record(self, cell_set, cell):
        self.set_for_cell[cell] = cell_set
        if cell_set not in self.cells_in_set:
            self.cells_in_set[cell_set] = []
        self.cells_in_set[cell_set].append(cell)

    def set_for(self, cell):
        if cell not in self.set_for_cell:
            self.record(self.next_set, cell)
            self.next_set += 1
        return self.set_for_cell[cell]

    def merge(self, winner, loser):
        for cell in self.cells_in_set[loser]:
            self.set_for_cell[cell] = winner
            self.cells_in_set[winner].append(cell)
        del self.cells_in_set[loser]

    def next(self):
        return RowState(self.next_set)

    def each_set(self):
        for cell_set, cells in self.cells_in_set.items():
            yield cell_set, cells


class EllersPolar:
    @staticmethod
    def on(grid: PolarGrid):
        """ Only 'from edge', for 'from center' see kotlin variant """
        row_state = RowState()
        for row in reversed(list(grid.each_row())):
            for cell in row:
                if not cell.cw:
                    continue
                cell_set = row_state.set_for(cell)
                prior_set = row_state.set_for(cell.cw)
                should_link = cell_set != prior_set and (cell.inward is None or randint(0, 1) == 0)
                if should_link:
                    cell.link(cell.cw)
                    row_state.merge(prior_set, cell_set)

            if row[0].inward:
                next_row = row_state.next()
                for _, cell_list in row_state.each_set():
                    for index, cell in enumerate(sample(cell_list, len(cell_list))):
                        current_set = row_state.set_for_cell[cell]
                        if index == 0 or randint(0, 2) == 0:
                            if cell.inward and current_set not in next_row.cells_in_set:
                                cell.link(cell.inward)
                                next_row.record(row_state.set_for(cell), cell.inward)
                row_state = next_row


def ellers_polar_demo():
    grid = PolarGrid(6)
    EllersPolar.on(grid)
    save(grid.to_img(), "ellers_polar.png")

    start = grid[0, 0]
    grid.set_distances(start.distances())
    save(grid.to_img(), "ellers_polar_colored.png")


if __name__ == "__main__":
    ellers_polar_demo()
