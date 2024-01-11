package com.example.gameboy

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
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
    var highscore = 0
    var highScoreListener: HighScoreListener? = null

    fun checkBounds(bounds: Rect): Boolean {
        var gameOver = false

        if (posY < bounds.top) {
            //puck has gone outside the top of screen
            highscore++
            Log.d("HighScore", "Current High Score: $highscore")
            highScoreListener?.onHighScoreUpdated(highscore)

            // Reset position
            resetPosition()

            return false  // Return false, the game is not over
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
        posX = 500f
        posY = 500f
    }

    fun update() {
        posY += speedY
        posX += speedX
    }

    fun draw(canvas: Canvas?) {
        canvas?.drawCircle(posX, posY, size, paint)
    }
}