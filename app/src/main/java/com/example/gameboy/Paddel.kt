package com.example.gameboy

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.shapes.Shape
class Paddle(context: Context, var posX: Float, var posY: Float, var width: Float, var height: Float, var speedY: Float) {

    var paint = Paint()

    fun checkBounds(bounds: Rect) {
        if (posX - size < 0) {
            this.speedX *= -1
            this.posX += speedX * 2
        }
        if (posX + size > bounds.right) {
            speedX *= -1
        }
        if (posY - size < 0) {
            speedY *= -1
        }
        if (posY + size > bounds.bottom) {
            speedY *= -1
        }
    }

    fun update() {

        posY += speedY
        posX += speedX
    }

    fun draw(canvas: Canvas?) {
        canvas?.drawRect(
            posX - width + height / 2,
            posY - width + height / 6,
            posX + width + height / 2,
            posY + width + height / 6,
            paint
        )
    }
}