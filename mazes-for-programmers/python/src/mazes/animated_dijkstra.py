from binary_tree import BinaryTree
from colored_grid import ColoredGrid


def create_animation(width, height):
    grid = ColoredGrid(width, height)
    BinaryTree.on(grid)

    size = grid.size()
    for depth in range(size):
        start = grid[10, 0]
        distances, frontier, new_frontier = start.distances(depth)
        grid.set_distances(distances)
        grid.set_frontiers(frontier, new_frontier)
        yield grid.to_img()


if __name__ == "__main__":
    it = create_animation(11, 11)
    img = next(it)
    img.save("images/animated_dijkstra.gif", format="GIF", append_images=it, save_all=True, duration=200, loop=0)
