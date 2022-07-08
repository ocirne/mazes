package io.github.ocirne.mazes.output

import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

fun createImage(width: Double, height: Double): Pair<BufferedImage, Graphics2D> {
    val image = BufferedImage(width.toInt(), height.toInt(), BufferedImage.TYPE_INT_ARGB)
    val g = image.createGraphics()
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
    g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
    g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON)
    return image to g
}

fun saveImage(image: BufferedImage, filename: String) {
    val path = "images/$filename.png"
    val outputfile = File(path)
    ImageIO.write(image, "png", outputfile)
    println("saved to $path")
}

fun BufferedImage.save(filename: String) {
    saveImage(this, filename)
}

fun formatForPages(images: List<BufferedImage>, columns: Int, rows: Int): BufferedImage {
    val largerImage = BufferedImage(740, 500, BufferedImage.TYPE_INT_ARGB)
    val g = largerImage.createGraphics()
    val width = images.maxOf { it.width }
    val height = images.maxOf { it.height }
    val gapX = (740 - width * columns) / (columns + 1)
    val gapY = (500 - height * rows) / (rows + 1)
    images.forEachIndexed { index, image ->
        val row = index / columns
        val col = index % columns
        val x = gapX + (width + gapX) * col
        val y = gapY + (height + gapY) * row
        g.drawImage(image, x + (width - image.width) / 2, y + (height - image.height) / 2, image.width, image.height, null)
    }
    return largerImage
}

fun BufferedImage.formatForPages(): BufferedImage {
    return formatForPages(listOf(this), 1, 1)
}
