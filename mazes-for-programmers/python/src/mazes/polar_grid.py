from datetime import datetime
from math import cos, sin, pi

from PIL import ImageColor, ImageDraw, Image

from grid import Grid


class PolarGrid(Grid):
    def to_png(self, cell_size=10, wall_size=3, filename=None, lfd=0, extension="png", save=True):
        img_size = 2 * self.rows * cell_size

        background = ImageColor.getrgb("white")
        wall = ImageColor.getrgb("black")

        img = Image.new("RGB", (img_size + 1, img_size + 1), background)
        draw = ImageDraw.Draw(img)

        center = img_size // 2

        for cell in self.each_cell():
            theta = 2 * pi / len(self.grid[cell.row])
            inner_radius = cell.row * cell_size
            outer_radius = (cell.row + 1) * cell_size
            theta_ccw = cell.column * theta
            theta_cw = (cell.column + 1) * theta

            ax = center + (inner_radius * cos(theta_ccw))
            ay = center + (inner_radius * sin(theta_ccw))
            # bx = center + (outer_radius * cos(theta_ccw))
            # by = center + (outer_radius * sin(theta_ccw))

            cx = center + (inner_radius * cos(theta_cw))
            cy = center + (inner_radius * sin(theta_cw))
            dx = center + (outer_radius * cos(theta_cw))
            dy = center + (outer_radius * sin(theta_cw))

            if not cell.is_linked(cell.north):
                draw.line((ax, ay, cx, cy), fill=wall, width=wall_size)
            if not cell.is_linked(cell.east):
                draw.line((cx, cy, dx, dy), fill=wall, width=wall_size)
        draw.ellipse((0, 0, img_size, img_size), width=wall_size, outline=wall)

        if save:
            if filename is None:
                filename = datetime.now().strftime("%%Y-%%m-%%d-%%H%%M%%S-%s.%s" % (lfd, extension))
            print("write to file", filename)
            img.save("images/" + filename)
        else:
            return img


# Demo
if __name__ == "__main__":
    grid = PolarGrid(8, 8)
    grid.to_png()
