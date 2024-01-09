package com.example.gameboy

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect

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
        canvas?.drawRect(posX - width / 2, posY - height / 2, posX + width / 2, posY + height / 2, paint)
    }
}