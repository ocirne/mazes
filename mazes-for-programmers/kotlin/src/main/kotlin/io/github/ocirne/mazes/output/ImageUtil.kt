package io.github.ocirne.mazes.output

import java.awt.Graphics
import java.awt.Point
import java.awt.image.RenderedImage
import java.io.File
import javax.imageio.ImageIO

fun saveImage(image: RenderedImage, filename: String) {
    val path = "images/$filename.png"
    val outputfile = File(path)
    ImageIO.write(image, "png", outputfile)
    println("saved to $path")
}

private fun drawline(g: Graphics, p1: Point, p2: Point) {
    g.drawLine(p1.x, p1.y, p2.x, p2.y)
}
