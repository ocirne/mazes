package io.github.ocirne.mazes

import java.awt.image.RenderedImage
import java.io.File
import javax.imageio.ImageIO

fun saveImage(image: RenderedImage, filename: String) {
    val outputfile = File("images/$filename.png")
    ImageIO.write(image, "png", outputfile)
}
