from random import choice
from grid import Grid
from image_saver import save
from colored_grid import ColoredGrid


class HuntAndKill:
    @staticmethod
    def on(grid: Grid):
        current = grid.random_cell()
        while current:
            unvisited_neighbors = [n for n in current.neighbors() if not n.links]
            if unvisited_neighbors:
                neighbor = choice(unvisited_neighbors)
                current.link(neighbor)
                current = neighbor
            else:
                current = None
                for cell in grid.each_cell():
                    visited_neighbors = [n for n in cell.neighbors() if n.links]
                    if not cell.links and visited_neighbors:
                        current = cell
                        neighbor = choice(visited_neighbors)
                        current.link(neighbor)
                        break
        return grid


def hunt_and_kill_demo():
    grid = ColoredGrid(11, 11)
    HuntAndKill.on(grid)
    save(grid.to_img(), "hunt_and_kill.png")

    middle = grid[grid.rows // 2, grid.columns // 2]
    grid.set_distances(middle.distances())

    save(grid.to_img(), "hunt_and_kill_colored.png")


if __name__ == "__main__":
    hunt_and_kill_demo()
