from distance_grid import DistanceGrid
from binary_tree import BinaryTree

grid = DistanceGrid(20, 40)
BinaryTree.on(grid)

start_helper = grid[0, 0]
distances = start_helper.distances()
start, _ = distances.max()

new_distances = start.distances()
goal, _ = new_distances.max()

grid.distances = new_distances.path_to(goal)
print(grid)
