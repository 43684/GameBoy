package com.example.gameboy

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView

class GameView(context: Context) : SurfaceView(context), SurfaceHolder.Callback, Runnable {

    private var thread: Thread? = null
    private var running = false
    lateinit var canvas: Canvas
    private lateinit var ball1: Ball
    private lateinit var padel1: Paddle
    private lateinit var padel2: Paddle
    private var bounds = Rect()
    var mHolder: SurfaceHolder? = holder



    init {
        if (mHolder != null) {
            mHolder?.addCallback(this)
        }
        setup()
    }

    private fun setup() {
        ball1 = Ball(this.context, 500f, 500f, 50f, -5f, 5f)
        padel1 = Paddle(this.context, 100f, 100f, 200f, 30f, 5f, 5f)
        padel2 = Paddle(this.context, 100f, 1200f, 200f, 30f, 5f, 5f)
        ball1.paint.color = Color.RED
        padel1.paint.color = Color.WHITE
        padel2.paint.color = Color.WHITE
    }

    fun ball(b1: Ball, b2: Paddle) {
        ball1.speedY *= -1
        padel1.speedY = 0f
        padel2.speedY = 0f
        //ball1.paint.color = Color.YELLOW
    }

    fun intersects(ball: Ball, padel: Paddle) {
        val closestX = clamp(ball.posX, padel.posX - padel.width / 2, padel.posX + padel.width / 2)
        val closestY = clamp(ball.posY, padel.posY - padel.height / 2, padel.posY + padel.height / 2)

        val distanceX = ball.posX - closestX
        val distanceY = ball.posY - closestY

        val distanceSquared = distanceX * distanceX + distanceY * distanceY

        if (distanceSquared <= (ball.size / 2) * (ball.size / 2)) {
            // Collision detected
            ball(ball, padel)
        }
    }

    // Helper function to clamp a value within a specified range
    fun clamp(value: Float, min: Float, max: Float): Float {
        return Math.max(min, Math.min(max, value))
    }

    fun start() {
        running = true
        thread = Thread(this)
        thread?.start()
    }

    fun stop() {
        running = false
        try {
            thread?.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    fun update() {
        ball1.update()
        padel1.update()
        padel2.update()
    }

    fun draw() {
        canvas = mHolder!!.lockCanvas()
        canvas.drawColor(Color.BLACK)
        ball1.draw(canvas)
        padel1.speedY = 0f;
        padel1.drawPadel(canvas)
        padel2.speedY = 0f
        padel2.drawPadel((canvas))
        mHolder!!.unlockCanvasAndPost(canvas)

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val sX = event?.x.toString()
        val sY = event?.y.toString()
        padel1.posX = sX.toFloat()
        //padel1.posY = sY.toFloat()
        padel2.posX = sX.toFloat()
        //padel2.posY = sY.toFloat()


        return true
    }

    override fun surfaceCreated(p0: SurfaceHolder) {

    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
        bounds = Rect(0, 0, p2, p3)
        start()
    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
        stop()
    }

    override fun run() {
        while (running) {
            update()
            draw()
            ball1.checkBounds(bounds)
            padel1.checkBounds(bounds)
            padel2.checkBounds(bounds)

            intersects(ball1, padel1)
            intersects(ball1, padel2)
        }
    }
}