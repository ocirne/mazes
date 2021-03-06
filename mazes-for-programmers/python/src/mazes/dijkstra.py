from distance_grid import DistanceGrid
from sidewinder import Sidewinder

grid = DistanceGrid(20, 20)
Sidewinder.on(grid)

start = grid[0, 0]
distances = start.distances()
grid.set_distances(distances)

print(grid)

grid.distances = distances.path_to(grid[grid.rows - 1, 0])
print(grid)

# print(grid.to_str_unicode())
