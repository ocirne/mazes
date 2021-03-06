from random import randint

from PIL import Image, ImageColor


class Mask:
    @staticmethod
    def from_txt(filename):
        lines = [line.strip() for line in open(filename).readlines()]
        rows = len(lines)
        columns = len(lines[0])
        mask = Mask(rows, columns)
        for row in range(rows):
            for col in range(columns):
                mask[row, col] = lines[row][col] == "."
        return mask

    @staticmethod
    def from_png(filename):
        image = Image.open(filename).convert("RGB")
        mask = Mask(image.height, image.width)
        pixels = image.load()
        for row in range(mask.rows):
            for col in range(mask.columns):
                mask[row, col] = pixels[col, row] == ImageColor.getrgb("black")
        return mask

    def __init__(self, rows, columns):
        self.rows = rows
        self.columns = columns
        self.bits = [[True for _ in range(columns)] for _ in range(rows)]

    def __getitem__(self, position):
        row, column = position
        if not 0 <= row < self.rows:
            return False
        if not 0 <= column < self.columns:
            return False
        return self.bits[row][column]

    def __setitem__(self, position, is_on):
        row, column = position
        self.bits[row][column] = is_on

    def count(self):
        return sum(self.bits)

    def random_location(self):
        while True:
            row = randint(0, self.rows - 1)
            column = randint(0, self.columns - 1)
            if self.bits[row][column]:
                return [row, column]
