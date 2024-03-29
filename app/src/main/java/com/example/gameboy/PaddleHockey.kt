package com.example.gameboy

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import org.checkerframework.checker.units.qual.min
import kotlin.math.min

class PaddleHockey (context: Context, var posX: Float, var posY: Float, var width: Float, var height: Float, var speedX: Float, var speedY: Float) {

    var paint = Paint()

    fun checkBounds(bounds: Rect) {
        if (posX - width / 2 < 0) {
            posX = width / 2
        }
        if (posX + width / 2 > bounds.right) {
            posX = bounds.right - width / 2
        }
        if (posY - height / 2 < 0) {
            posY = height / 2
        }
        if (posY + height / 2 > bounds.bottom) {
            posY = bounds.bottom - height / 2
        }
    }

    fun drawPaddleHockey(canvas: Canvas?) {
        val outerRadius = min(width, height) / 2 * 1.5
        val innerRadius = outerRadius * 0.5
        val centerX = posX
        val centerY = posY


        canvas?.drawCircle(centerX, centerY, outerRadius.toFloat(), paint)


        paint.color = Color.rgb(200,0,0)
        canvas?.drawCircle(centerX, centerY, innerRadius.toFloat(), paint)


        paint.color = Color.rgb(220,0,0)
    }

    fun update(){
posX+=speedX

    }
}

