package com.example.gameboy

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import androidx.fragment.app.Fragment

class Puck(
    context: Context,
    var posX: Float,
    var posY: Float,
    var size: Float,
    var speedX: Float,
    var speedY: Float
) : Fragment() {

    var paint = Paint()

    fun update() {
        posY += speedY
        posX += speedX
    }

    fun draw(canvas: Canvas?) {

    }
}