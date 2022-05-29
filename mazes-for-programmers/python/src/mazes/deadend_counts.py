from statistics import mean, stdev

from grid import Grid
from binary_tree import BinaryTree
from sidewinder import Sidewinder
from aldous_broder import AldousBroder
from wilsons import Wilsons
from hunt_and_kill import HuntAndKill

algorithms = [BinaryTree, Sidewinder, AldousBroder, Wilsons, HuntAndKill]

tries = 100
size = 20

averages = {}
standard_deviation = {}

for algorithm in algorithms:
    print("running %s ..." % algorithm.__name__)
    deadend_counts = []
    for _ in range(tries):
        grid = Grid(size, size)
        algorithm.on(grid)
        deadend_counts.append(len(grid.deadends()))

    averages[algorithm] = mean(deadend_counts)
    standard_deviation[algorithm] = stdev(deadend_counts)

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
