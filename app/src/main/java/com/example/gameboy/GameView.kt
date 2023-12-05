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
    private lateinit var ball: Ball
    private lateinit var paddle1: Paddle
    private lateinit var paddle2: Paddle
    private var bounds = Rect()
    var mHolder: SurfaceHolder? = holder

    init {
        if (mHolder != null) {
            mHolder?.addCallback(this)
        }
        setup()
    }

    private fun setup() {
        ball = Ball(this.context, 100f, 100f, 50f, -5f, 5f)
        paddle1 = Paddle(this.context, 100f, 200f, 130f, 5f, 0f)
        paddle2 = Paddle(this.context, 100f, 1000f, 130f, 5f, 0f)
        ball.paint.color = Color.RED
        paddle1.paint.color = Color.BLACK
        paddle2.paint.color = Color.BLACK
    }

    fun ball(b1: Ball, b2: Paddle) {
        ball.speedY *= -1
        paddle1.speedY = 0f
        paddle2.speedY = 0f
    }

    fun intersects(b1: Ball, b2: Paddle) {
        if (Math.sqrt(
                Math.pow(
                    b1.posX - b2.posX.toDouble(),
                    2.0
                ) + Math.pow(b1.posY - b2.posY.toDouble(), 2.0)
            ) <= b1.size + b2.size
        ) {
            ball(b1, b2)
        }
    }

    private fun handleCollision(player: Paddle?, ball: Ball?) {
        ball!!.speedX = -ball.speedX * 1.05f
        if (player === this.paddle1) {
            ball.cx = player!!.bounds.right + ball.radius
        } else if (player === mOpponent) {
            ball.cx = mOpponent!!.bounds.left - ball.radius
            PHY_RACQUET_SPEED *= 1.05f
        }
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
        ball.update()
        paddle1.update()
        paddle2.update()
    }

    fun draw() {
        canvas = mHolder!!.lockCanvas()
        canvas.drawColor(Color.WHITE)
        ball.draw(canvas)
        paddle1.speedY = 0f
        paddle1.draw(canvas)
        paddle2.speedY = 0f
        paddle2.draw(canvas)
        mHolder!!.unlockCanvasAndPost(canvas)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val sX = event?.x.toString()
       // val sY = event?.y.toString()
        paddle1.posX = sX.toFloat()
        paddle2.posX = sX.toFloat()
        //paddle.posY = sY.toFloat()

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
            ball.checkBounds(bounds)
            paddle1.checkBounds(bounds)
            paddle2.checkBounds(bounds)
            intersects(ball, paddle1)
            intersects(ball, paddle2)
        }
    }
}