package com.example.gameboy

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import androidx.fragment.app.Fragment
class Puck(
    context: Context,
    var screenWidth: Int,
    var screenHeight: Int,
    var posX: Float,
    var posY: Float,
    var size: Float,
    var speedX: Float,
    var speedY: Float
) : Fragment() {

    var paint = Paint()

    fun checkBounds(bounds: Rect): Boolean {
        var gameOver = false

        if (posY < bounds.top) {
            // The puck has gone outside the top of the screen
            resetPosition()
            return false  // Return false to indicate that the game is not over
        }

        if (posX - size < 0) {
            this.speedX *= -1f
            this.posX += speedX * 2
        }
        if (posX + size > bounds.right) {
            this.speedX *= -1f
            this.posX += speedX * 2
        }
        if (posY + size > bounds.bottom) {
            speedX = 0f
            speedY = 0f
            gameOver = true
        }

        return gameOver
    }

    fun resetPosition() {
        posX = screenWidth / 2f
        posY = screenHeight / 2f
    }

    fun update() {
        posY += speedY
        posX += speedX
    }

    fun draw(canvas: Canvas?) {
        canvas?.drawCircle(posX, posY, size, paint)
    }
}