package com.example.gameboy

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.core.graphics.alpha
import androidx.fragment.app.findFragment

import java.lang.Math.atan2
import java.lang.Math.cos
import java.lang.Math.sin
import java.lang.Math.sqrt
import kotlin.math.sqrt

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


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
    private var collisionCooldown = false
    private val collisionCooldownTime = 500L


    init {
        if (mHolder != null) {
            mHolder?.addCallback(this)
        }
        setup()
    }

    private fun setup() {
        ball = Ball(this.context, 500f, 500f, 50f, -5f, 5f)
        padel1 = Paddle(this.context, 500f, 200f, 200f, 20f, 5f, 5f)
        padel2 = Paddle(this.context, 500f, 1400f, 200f, 20f, 5f, 5f)
        ball.paint.color = Color.RED
        padel1.paint.color = Color.WHITE
        padel2.paint.color = Color.WHITE
    }

    fun ball(b1: Ball, b2: Paddle) {
        ball.speedY *= -1
        padel1.speedY = 0f
        padel2.speedY = 0f
    }

    fun intersects(ball: Ball, paddle: Paddle) {
        if (ball.posY > 199 || ball.posY < 1401) {
            val closestX = clamp(ball.posX, paddle.posX - paddle.width / 2, paddle.posX + paddle.width / 2)
            val closestY = clamp(ball.posY, paddle.posY - paddle.height / 2, paddle.posY + paddle.height / 2)

            val distanceX = ball.posX - closestX
            val distanceY = ball.posY - closestY

            val distanceSquared = distanceX * distanceX + distanceY * distanceY

            if (distanceSquared <= (ball.size / 2) * (ball.size / 2) && !collisionCooldown) {
                // Handle collision
                ball(ball, paddle)
                highscore++
                Log.d("HighScore", "Current High Score: $highscore")

                highScoreListener?.onHighScoreUpdated(highscore)

                if (highscore > 1 && highscore % 2 == 0) {
                    ball.speedX *= 1.2f
                    ball.speedY *= 1.2f
                }


                // Update ball position based on collision point
                val angle = atan2(distanceY.toDouble(), distanceX.toDouble())
                val overlap = (ball.size / 2) - sqrt(distanceSquared)
                val newX = ball.posX + (overlap * cos(angle)).toFloat()
                val newY = ball.posY + (overlap * sin(angle)).toFloat()

                ball.posX = newX
                ball.posY = newY

                // Set collision cooldown
                collisionCooldown = true
                Handler(Looper.getMainLooper()).postDelayed({
                    collisionCooldown = false
                }, collisionCooldownTime)
            }
        }
    }

    fun clamp(value: Float, min: Float, max: Float): Float {
        return if (value < min) min else if (value > max) max else value
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
        canvas.drawColor(Color.argb(128, 0, 0, 0))
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

        updateHighScore(highscore)


    }

    interface VisibilityListener {
        fun makeVisible()
    }

    fun updateHighScore(newHighScore: Int) {
        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        val database: DatabaseReference = FirebaseDatabase.getInstance().reference
        // Get the current user's UID from FirebaseAuth
        val uid: String? = auth.currentUser?.uid

        if (uid != null) {
            // Reference to the user's data in the Firebase database
            val userRef: DatabaseReference = database.child("users").child(uid)

            // Read the current high score from the database
            userRef.child("highscore").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val currentHighScore = dataSnapshot.getValue(Int::class.java)

                    // Check if the new high score is higher than the existing one
                    if (currentHighScore == null || newHighScore > currentHighScore) {
                        // Update the high score in the database
                        userRef.child("highscore").setValue(newHighScore)
                            .addOnSuccessListener {
                                println("High score updated successfully!")
                            }
                            .addOnFailureListener { e ->
                                println("Error updating high score: ${e.message}")
                            }
                    } else {
                        println("New high score is not higher than the existing one.")
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    println("Error reading user data: ${databaseError.message}")
                }
            })
        } else {
            println("User not authenticated.")
        }
    }


}
