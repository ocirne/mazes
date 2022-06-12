from statistics import mean, stdev

from grid import Grid
from binary_tree import BinaryTree
from sidewinder import Sidewinder
from aldous_broder import AldousBroder
from wilsons import Wilsons
from hunt_and_kill import HuntAndKill
from recursive_backtracker import RecursiveBacktracker

algorithms = [BinaryTree, Sidewinder, AldousBroder, Wilsons, HuntAndKill, RecursiveBacktracker]

tries = 100
size = 20

averages = {}
standard_deviation = {}

for algorithm in algorithms:
    print("running %s ..." % algorithm.__name__)
    dead_end_counts = []
    for _ in range(tries):
        grid = Grid(size, size)
        algorithm.on(grid)
        dead_end_counts.append(len(grid.dead_ends()))

    averages[algorithm] = mean(dead_end_counts)
    standard_deviation[algorithm] = stdev(dead_end_counts)

total_cells = size * size
print()
print("Average dead-ends per %sx%s maze %s (sample size %s)" % (size, size, total_cells, tries))
print()

sorted_algorithms = sorted(algorithms, key=lambda algorithm: -averages[algorithm])

for algorithm in sorted_algorithms:
    percentage = averages[algorithm] * 100.0 / total_cells
    print(
        "%14s : %3d/%d (std %.3f) (%d%%)"
        % (algorithm.__name__, averages[algorithm], total_cells, standard_deviation[algorithm], percentage)
    )
