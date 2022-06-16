package io.github.ocirne.mazes

import java.awt.image.RenderedImage
import java.io.File
import javax.imageio.ImageIO

fun saveImage(image: RenderedImage, filename: String) {
    val path = "images/$filename.png"
    val outputfile = File(path)
    ImageIO.write(image, "png", outputfile)
    println("saved to $path")
}
