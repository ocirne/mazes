package io.github.ocirne.mazes.grids

class UpsilonCell(val row: Int, val column: Int) : Cell() {

    var north: UpsilonCell? = null
    var northeast: UpsilonCell? = null
    var east: UpsilonCell? = null
    var southeast: UpsilonCell? = null
    var south: UpsilonCell? = null
    var southwest: UpsilonCell? = null
    var west: UpsilonCell? = null
    var northwest: UpsilonCell? = null

    override fun neighbors(): List<UpsilonCell> {
        return arrayListOf(north, northeast, east, southeast, south, southwest, west, northwest).filterNotNull()
    }

    fun isOctogon(): Boolean {
        return (row + column) % 2 == 0
    }
}
