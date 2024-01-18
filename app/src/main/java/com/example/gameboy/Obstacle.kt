package com.example.gameboy

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathMeasure
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Region
import android.graphics.RegionIterator
import java.lang.Math.abs

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
        val puckRadius = puck.size / 2
        val puckCenterX = puck.posX
        val puckCenterY = puck.posY

        val closestX = clamp(puckCenterX, posX, posX + width)
        val closestY = clamp(puckCenterY, posY, posY + height)

        val distanceX = puckCenterX - closestX
        val distanceY = puckCenterY - closestY

        val distanceSquared = distanceX * distanceX + distanceY * distanceY

        return distanceSquared <= puckRadius * puckRadius
    }

    private fun clamp(value: Float, min: Float, max: Float): Float {
        return if (value < min) min else if (value > max) max else value

    }
}