from heapq import heappush, heappop

from cell import Cell
from distances import Distances


class WeightedCell(Cell):
    def __init__(self, row, column):
        super().__init__(row, column)
        self.weight = 1

    def distances(self):
        weights = Distances(self)
        pending = []
        heappush(pending, (1, self))
        while pending:
            f, cell = heappop(pending)
            for neighbor in cell.links:
                total_weight = weights[cell] + neighbor.weight
                if neighbor not in weights or total_weight < weights[neighbor]:
                    heappush(pending, (total_weight, neighbor))
                    weights[neighbor] = total_weight
        return weights

    def __gt__(self, other):
        return self.weight < other.weight
