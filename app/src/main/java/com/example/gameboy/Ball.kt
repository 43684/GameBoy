package com.example.gameboy

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect

class Ball (context: Context, var posX: Float, var posY: Float, var size: Float, var speedX: Float, var speedY: Float ){


    //var posX = 0f
    //    var posY = 0f
    //    var paint = Paint()
    //    var size = 50f
    //    var speed = 5f


    var paint = Paint()

    fun checkBounds(bounds: Rect){
        if (posX - size <0){
            this.speedX *= -1
            this.posX += speedX *2
        }
        if (posX + size > bounds.right){
            speedX *= -1
        }
        if (posY - size <0){
            speedY *= -1
        }
        if (posY + size > bounds.bottom){
            speedY *= -1
        }
    }
    fun update(){

        posY += speedY
        posX += speedX

    }

    fun draw(canvas: Canvas?){

        canvas?.drawCircle(posX, posY, size, paint)
        

    }
}