from random import choice, randint
from grid import Grid


class Sidewinder:
    @staticmethod
    def on(grid):
        for row in grid.each_row():
            run = []
            for cell in row:
                run.append(cell)
                at_eastern_boundary = cell.east is None
                at_northern_boundary = cell.north is None
                should_close_out = at_eastern_boundary or (
                    not at_northern_boundary and randint(0, 2) == 0
                )
                if should_close_out:
                    member = choice(run)
                    if member.north:
                        member.link(member.north)
                    run = []
                else:
                    cell.link(cell.east)
        return grid


if __name__ == "__main__":
    grid = Grid(10, 10)
    Sidewinder().on(grid)
    print(grid)

    grid.to_png()
