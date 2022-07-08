package io.github.ocirne.mazes.output

import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.awt.image.RenderedImage
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

fun resizeImage(image: BufferedImage): RenderedImage {
    val (largerImage, g) = createImage(740.0, 500.0)
    val x = (740 - image.width) / 2
    val y = (500 - image.height) / 2
    g.drawImage(image, x, y, image.width, image.height, null)
    return largerImage
}

fun saveImage(image: BufferedImage, filename: String) {
    val largerImage = resizeImage(image)
    val path = "images/$filename.png"
    val outputfile = File(path)
    ImageIO.write(largerImage, "png", outputfile)
    println("saved to $path")
}

fun BufferedImage.save(filename: String) {
    saveImage(this, filename)
}

fun combineToImage(images: List<BufferedImage>): BufferedImage {
    val largerImage = BufferedImage(740, 500, BufferedImage.TYPE_INT_ARGB)
    val g = largerImage.createGraphics()
    val width = images[0].width
    val height = images[0].height
    val gapX = (740 - width * 3) / 4
    val gapY = (500 - height * 2) / 3
    images.forEachIndexed { index, image ->
        val row = index / 3
        val col = index % 3
        val x = gapX + (width + gapX) * col
        val y = gapY + (height + gapY) * row
        g.drawImage(image, x, y, width, height, null)
    }
    return largerImage
}
