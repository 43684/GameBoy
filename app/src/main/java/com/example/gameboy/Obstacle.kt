package com.example.gameboy

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF

class Obstacle(
    var posX: Float,
    var posY: Float,
    var width: Float,
    var height: Float,
    var color: Int
) {
    val paint = Paint()

    init {
        paint.color = color
    }

    fun draw(canvas: Canvas) {
        canvas.drawRect(posX, posY, posX + width, posY + height, paint)
    }

    fun intersects(puck: Puck): Boolean {
        return RectF(posX, posY, posX + width, posY + height).intersects(
            puck.posX - puck.size / 2,
            puck.posY - puck.size / 2,
            puck.posX + puck.size / 2,
            puck.posY + puck.size / 2
        )
    }
}