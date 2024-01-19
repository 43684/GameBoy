package com.example.gameboy

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.fragment.app.findFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.math.abs
import kotlin.math.sqrt

class GameViewHockey(context: Context, var listener: HighScoreListener) : SurfaceView(context), SurfaceHolder.Callback, Runnable {

    private var thread: Thread? = null
    private var running = false
    lateinit var canvas: Canvas
    private lateinit var puck: Puck
    private lateinit var paddle1: PaddleHockey
    private lateinit var paddle2: PaddleHockey
    var mHolder: SurfaceHolder? = holder
    private var bounds = Rect()
    var highscore = 0
    var highScoreListener: HighScoreListener? = null
    private var collisionCooldown = false
    private val collisionCooldownTime = 250L
    var bitmap: Bitmap
    private val obstacles = mutableListOf<Obstacle>()
    init {
        if (mHolder != null) {
            mHolder?.addCallback(this)
        }
        obstacles.add(Obstacle(585f, 1050f, 120f, 120f, Color.BLUE))

        bitmap = BitmapFactory.decodeResource(context.resources,R.drawable.edited2)
        setup()
    }
    private fun setup() {
        paddle1 = PaddleHockey(this.context, 500f, 200f, 200f, 100f, 5f, 5f)
        paddle2 = PaddleHockey(this.context, 500f, 1600f, 200f, 100f, 5f, 5f)
        paddle1.paint.color = Color.RED
        paddle2.paint.color = Color.RED
        puck = Puck(this.context, listener,500f, 500f, 50f, -7f, 7f)
        puck.paint.color = Color.BLACK
    }

    fun intersects(puck: Puck, paddle: PaddleHockey) {
        val closestX = clamp(puck.posX, paddle.posX - paddle.width / 2, paddle.posX + paddle.width / 2)
        val closestY = clamp(puck.posY, paddle.posY - paddle.height / 2, paddle.posY + paddle.height / 2)

        val distanceX = puck.posX - closestX
        val distanceY = puck.posY - closestY

        val distanceSquared = distanceX * distanceX + distanceY * distanceY

        if (distanceSquared <= (puck.size / 2) * (puck.size / 2) && !collisionCooldown) {


            puckCollision(puck, paddle)


            collisionCooldown = true
            Handler(Looper.getMainLooper()).postDelayed({
                collisionCooldown = false
            }, collisionCooldownTime)
        }
    }
    fun clamp(value: Float, min: Float, max: Float): Float {
        return if (value < min) min else if (value > max) max else value
    }

    fun puckCollision(puck: Puck, paddle: PaddleHockey) {
        if (puck.posX < paddle.posX) {
            // Left side collision
            if (puck.posY < paddle.posY) {
                // Top-left corner
                puck.speedX = abs(puck.speedX) * -1
                puck.speedY = abs(puck.speedY) * -1
            } else {
                // Bottom-left corner
                puck.speedX = abs(puck.speedX) * -1
                puck.speedY = abs(puck.speedY)
            }
        } else {
            // Right side collision
            if (puck.posY > paddle.posY) {
                // Bottom-right corner
                puck.speedX = abs(puck.speedX)
                puck.speedY = abs(puck.speedY)
            } else {
                // Top-right corner
                puck.speedX = abs(puck.speedX)
                puck.speedY = abs(puck.speedY) * -1
            }
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
        puck.update()
        paddle1.update()
        updateObstacles()

        if (paddle1.posX - paddle1.width / 2 < bounds.left) {
            paddle1.speedX = abs(paddle1.speedX)  // Bounce off the left wall
        }

        else if (paddle1.posX + paddle1.width / 2 > bounds.right) {
            paddle1.speedX = -abs(paddle1.speedX)  // Bounce off the right wall
        }
    }
    private fun checkObstacleCollisions() {

        for (obstacle in obstacles) {
            if (obstacle.intersects(puck)) {
                val random1 = -1f
                val random2 = 1f

                val hockeyList = mutableListOf<Float>(random1, random2)

                puck.speedX *= hockeyList.random()


            }
        }
    }

    fun draw() {
        canvas = mHolder!!.lockCanvas()
        canvas?.drawBitmap(bitmap,0f,0f, null)
        puck.draw(canvas)
        paddle1.speedY = 0f
        paddle1.drawPaddleHockey(canvas)
        paddle2.speedY = 0f
        paddle2.drawPaddleHockey(canvas)
        drawObstacles(canvas) // Rita hindren
        mHolder!!.unlockCanvasAndPost(canvas)

        invalidate()

    }
    private fun drawObstacles(canvas: Canvas) {
        for (obstacle in obstacles) {
            obstacle.draw(canvas)
        }
    }
    private fun updateObstacles() {
    }
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null && event.y > height / 2) {
            val touchX = event.x
            val touchY = event.y
            paddle2.posX = touchX
            paddle2.posY = touchY


        }
        return true
    }
    override fun surfaceCreated(holder: SurfaceHolder) {

    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
        bounds = Rect(0, 0, p2, p3)
        start()
    }
    override fun surfaceDestroyed(holder: SurfaceHolder) {
        stop()
    }

    override fun run() {
        while (running) {
            update()
            draw()
            val gameOver = puck.checkBounds(bounds)
            if (gameOver) {

                onGameOver()
            }

            paddle1.checkBounds(bounds)
            paddle2.checkBounds(bounds)

            intersects(puck, paddle1)
            intersects(puck, paddle2)

            checkObstacleCollisions()
        }
    }
    fun onGameOver() {
        println("onGameOver")
        running = false
        findFragment<PlayHockeyFragment>().makeVisible()



    }


}
