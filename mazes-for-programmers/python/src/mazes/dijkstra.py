from distance_grid import DistanceGrid
from sidewinder import Sidewinder

grid = DistanceGrid(5, 5)
Sidewinder.on(grid)

start = grid[0, 0]
distances = start.distances()
grid.set_distances(distances)

print(grid)
print(grid.to_str_unicode())
