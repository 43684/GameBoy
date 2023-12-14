package com.example.gameboy

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import androidx.fragment.app.Fragment

class Ball (context: Context, var posX: Float, var posY: Float, var size: Float, var speedX: Float, var speedY: Float ): Fragment(){

    var listener: GameListener? = null
    var paint = Paint()

    var highScoreListener: HighScoreListener? = null



    override fun onAttach(context: Context){
        super.onAttach(context)

        try{
            listener = context as GameListener
            println("Successful implementation")
        } catch (e: Exception){
            println("Failed implementation")
        }

    }
    fun checkBounds(bounds: Rect){
        if (posX - size <0){
            this.speedX *= -1
            this.posX += speedX *2
        }
        if (posX + size > bounds.right){
            speedX *= -1

        }
        if (posY - size <0){
            posY = bounds.exactCenterY()
            posX = bounds.exactCenterX()
            speedX = 0f
            speedY = 0f


        }
        if (posY + size > bounds.bottom){
            posY = bounds.exactCenterY()
            posX = bounds.exactCenterX()
            speedX = 0f
            speedY = 0f

        }
    }

    fun update(){

        posY += speedY
        posX += speedX

    }

    fun draw(canvas: Canvas?){

        canvas?.drawCircle(posX, posY, size, paint)
    }


    interface GameListener{
        fun startPongMenu()

    }


}