package io.github.ocirne.mazes.output

import java.awt.image.RenderedImage
import java.io.File
import javax.imageio.ImageIO

fun saveImage(image: RenderedImage, filename: String) {
    val path = "images/$filename.png"
    val outputfile = File(path)
    ImageIO.write(image, "png", outputfile)
    println("saved to $path (${image.width} x ${image.height})")
}

fun RenderedImage.save(filename: String) {
    saveImage(this, filename)
}
