from random import seed
from statistics import mean, stdev

from grid import Grid
from binary_tree import BinaryTree
from sidewinder import Sidewinder
from aldous_broder import AldousBroder
from wilsons import Wilsons
from hunt_and_kill import HuntAndKill
from recursive_backtracker import RecursiveBacktracker


def count_dead_ends_demo():
    """
    >>> seed(42)
    >>> count_dead_ends_demo()
    running BinaryTree ...
    running Sidewinder ...
    running AldousBroder ...
    running Wilsons ...
    running HuntAndKill ...
    running RecursiveBacktracker ...
    <BLANKLINE>
    Average dead-ends per 20x20 maze 400 (sample size 100)
    <BLANKLINE>
                 Wilsons: 116/400 (std 5.822) (29%)
            AldousBroder: 115/400 (std 5.094) (28%)
              Sidewinder: 107/400 (std 6.170) (26%)
              BinaryTree: 100/400 (std 4.973) (25%)
    RecursiveBacktracker:  42/400 (std 3.782) (10%)
             HuntAndKill:  39/400 (std 3.765) (9%)
    """
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
    print("\nAverage dead-ends per %sx%s maze %s (sample size %s)\n" % (size, size, total_cells, tries))

    sorted_algorithms = sorted(algorithms, key=lambda algo: -averages[algo])

    for algorithm in sorted_algorithms:
        percentage = averages[algorithm] * 100.0 / total_cells
        print(
            "%20s: %3d/%d (std %.3f) (%d%%)"
            % (algorithm.__name__, averages[algorithm], total_cells, standard_deviation[algorithm], percentage)
        )


if __name__ == "__main__":
    count_dead_ends_demo()
