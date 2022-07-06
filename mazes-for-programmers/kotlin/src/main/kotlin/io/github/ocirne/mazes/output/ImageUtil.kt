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
    val largerImage = BufferedImage(800, 400, BufferedImage.TYPE_INT_ARGB)
    val g = largerImage.createGraphics()
    val x = (800 - image.width) / 2
    val y = (400 - image.height) / 2
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
