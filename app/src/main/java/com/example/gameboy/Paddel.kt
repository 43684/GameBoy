package com.example.gameboy

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.shapes.Shape
class Paddle(context: Context, var posX: Float, var posY: Float, var width: Float, var height: Float, var speedX: Float, var speedY: Float) {

    var paint = Paint()

    fun checkBounds(bounds: Rect) {
        if (posX - width / 2 < 0) {
            speedX *= -1
            posX = width / 2 // Adjusting the position to prevent sticking at the boundary
        }
        if (posX + width / 2 > bounds.right) {
            speedX *= -1
            posX = bounds.right - width / 2 // Adjusting the position to prevent sticking at the boundary
        }
        if (posY - height / 2 < 0) {
            speedY *= -1
            posY = height / 2 // Adjusting the position to prevent sticking at the boundary
        }
        if (posY + height / 2 > bounds.bottom) {
            speedY *= -1
            posY = bounds.bottom - height / 2 // Adjusting the position to prevent sticking at the boundary
        }
    }

    fun drawPadel(canvas: Canvas?) {
        canvas?.drawRect(posX - width / 2, posY - height / 2, posX + width / 2, posY + height / 2, paint)
    }

    fun update() {


    }

}