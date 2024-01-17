package com.example.gameboy

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
import androidx.fragment.app.Fragment
class Puck(
    context: Context,
    var highScoreListener: HighScoreListener ,
    var posX: Float,
    var posY: Float,
    var size: Float,
    var speedX: Float,
    var speedY: Float
) : Fragment() {

    var paint = Paint()
    var highscore = 0

    fun checkBounds(bounds: Rect): Boolean {
        var gameOver = false

        if(posX > 500f && posX < 800f){
        if (posY < bounds.top) {
            //puck has gone outside the top of screen
            highscore++
            Log.d("HighScore", "Current High Score (puck): $highscore")
            highScoreListener?.onHighScoreUpdated(highscore)

            // Reset position
            resetPosition()

            return false  // Return false, game is not over
        }
        }else{
            if(posY < bounds.top){
                speedY *= -1f
            }
        }


        if (posX - size < 0) {
            this.speedX *= -1f
            this.posX += speedX * 2
        }
        if (posX + size > bounds.right) {
            this.speedX *= -1f
            this.posX += speedX * 2
        }

        if (posX > 500f && posX < 800f){


            if (posY + size > bounds.bottom) {

                gameOver = true
            }
        }else {
            if (posY + size > bounds.bottom) {

                speedY *= -1f
            }

        }
        return gameOver
    }

    fun resetPosition() {
        posX = 500f
        posY = 500f
        speedY*=-1f
    }

    fun update() {
        posY += speedY
        posX += speedX

    }

    fun draw(canvas: Canvas?) {
        canvas?.drawCircle(posX, posY, size, paint)
    }
}