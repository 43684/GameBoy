package com.example.gameboy

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.fragment.app.findFragment

class GameView(context: Context) : SurfaceView(context), SurfaceHolder.Callback, Runnable {

    private var thread: Thread? = null
    private var running = false
    lateinit var canvas: Canvas
    private lateinit var ball: Ball
    private lateinit var padel1: Paddle
    private lateinit var padel2: Paddle
    private var bounds = Rect()
    var mHolder: SurfaceHolder? = holder
    var highscore = 0
    var highScoreListener: HighScoreListener? = null
    var saveHighscore: Int = 0


    init {
        if (mHolder != null) {
            mHolder?.addCallback(this)
        }
        setup()
    }

    private fun setup() {
        ball = Ball(this.context, 500f, 500f, 50f, -5f, 5f)
        padel1 = Paddle(this.context, 500f, 200f, 200f, 30f, 5f, 5f)
        padel2 = Paddle(this.context, 500f, 1400f, 200f, 30f, 5f, 5f)
        ball.paint.color = Color.RED
        padel1.paint.color = Color.WHITE
        padel2.paint.color = Color.WHITE
    }

    fun ball(b1: Ball, b2: Paddle) {
        ball.speedY *= -1
        padel1.speedY = 0f
        padel2.speedY = 0f
    }

    fun intersects(ball: Ball, padel: Paddle) {

        if (ball.posY < 1000 || ball.posY > 1000 ) {
            val closestX = clamp(ball.posX, padel.posX - padel.width, padel.posX + padel.width)
            val closestY =
                clamp(ball.posY, padel.posY - padel.height, padel.posY + padel.height)

            val distanceX = ball.posX - closestX
            val distanceY = ball.posY - closestY

            val distanceSquared = distanceX * distanceX + distanceY * distanceY

            if (distanceSquared <= (ball.size / 2) * (ball.size / 2)) {
                ball(ball, padel)
                highscore++
                Log.d("HighScore", "Current High Score: $highscore")

                highScoreListener?.onHighScoreUpdated(highscore)

                if (highscore > 1 && highscore % 2 == 0) {
                    ball.speedX *= 1.2f
                    ball.speedY *= 1.2f
                }

                if (saveHighscore < highscore) {
                    saveHighscore = highscore
                    Log.d("saveHighscore", "ssved highschore: $highscore")
                }
            }
        }
    }

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
        ball.update()
        padel1.update()
        padel2.update()
    }

    fun draw() {
        canvas = mHolder!!.lockCanvas()
        canvas.drawColor(Color.BLACK)
        ball.draw(canvas)
        padel1.speedY = 0f
        padel1.drawPadel(canvas)
        padel2.speedY = 0f
        padel2.drawPadel((canvas))
        mHolder!!.unlockCanvasAndPost(canvas)

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val sX = event?.x.toString()
        padel1.posX = sX.toFloat()
        padel2.posX = sX.toFloat()

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
            // Log.d("GameView", "Updating and Drawing")
            update()
            draw()
            val gameOver = ball.checkBounds(bounds)
            if (gameOver) {

                onGameOver()
            }

            padel1.checkBounds(bounds)
            padel2.checkBounds(bounds)


            intersects(ball, padel1)
            intersects(ball, padel2)

        }
    }

    fun onGameOver() {
        println("onGameOver")
        running = false
        findFragment<PlayPongFragment>().makeVisible()

        /**
         *  updateData()
          */


    }

    interface VisibilityListener {
        fun makeVisible()
    }

}
