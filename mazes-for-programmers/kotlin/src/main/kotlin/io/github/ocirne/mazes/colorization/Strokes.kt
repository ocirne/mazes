package io.github.ocirne.mazes.colorization

import java.awt.BasicStroke
import java.awt.Stroke

class Strokes(val cellSize: Float) {

    fun getBasicWall(): Stroke {
        return BasicStroke(0.1f * cellSize)
    }

    fun getHiddenWall(): Stroke {
        return BasicStroke(0.1f * cellSize, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0.0f, floatArrayOf(2.0f), 0.0f)
    }

    fun getPath(): Stroke {
        return BasicStroke(0.5f * cellSize)
    }
}