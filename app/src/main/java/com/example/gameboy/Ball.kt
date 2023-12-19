package com.example.gameboy

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.ActionProvider.VisibilityListener
import androidx.fragment.app.Fragment

class Ball(
    context: Context,
    var posX: Float,
    var posY: Float,
    var size: Float,
    var speedX: Float,
    var speedY: Float
) : Fragment() {

    var paint = Paint()

    fun checkBounds(bounds: Rect): Boolean {
        var gameOver = false

        if (posX - size < 0) {
            this.speedX *= -1f
            this.posX += speedX * 2
        }
        if (posX + size > bounds.right) {
            this.speedX *= -1f
            this.posX += speedX * 2

        }
        if (posY - size < 0 || posY + size > bounds.bottom) {
            speedX = 0f
            speedY = 0f

            gameOver = true

        }
        return gameOver
    }

    fun update() {

        posY += speedY
        posX += speedX

    }

    fun draw(canvas: Canvas?) {

        canvas?.drawCircle(posX, posY, size, paint)
    }
}