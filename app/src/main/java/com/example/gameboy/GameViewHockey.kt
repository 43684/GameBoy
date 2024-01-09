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
import androidx.core.math.MathUtils.clamp
import androidx.fragment.app.findFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.math.sqrt

class GameViewHockey(context: Context) : SurfaceView(context), SurfaceHolder.Callback, Runnable {

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
    private val collisionCooldownTime = 500L

    init {
        if (mHolder != null) {
            mHolder?.addCallback(this)
        }
        setup()
    }
    private fun setup() {
        paddle1 = PaddleHockey(this.context, 500f, 200f, 200f, 20f, 5f, 5f)
        paddle2 = PaddleHockey(this.context, 500f, 1600f, 200f, 20f, 5f, 5f)
        paddle1.paint.color = Color.RED
        paddle2.paint.color = Color.WHITE
        puck = Puck(this.context, 500f, 500f, 50f, -5f, 5f)
        puck.paint.color = Color.RED
    }

    fun puck(b1: Puck, b2: PaddleHockey) {
        puck.speedY *= -1
        paddle1.speedY = 0f
        paddle2.speedY = 0f
    }
    fun intersects(puck:Puck, paddle: PaddleHockey) {
        if (puck.posY > 199 || puck.posY < 2001) {
            val closestX = clamp(puck.posX, paddle.posX - paddle.width / 2, paddle.posX + paddle.width / 2)
            val closestY = clamp(puck.posY, paddle.posY - paddle.height / 2, paddle.posY + paddle.height / 2)

            val distanceX = puck.posX - closestX
            val distanceY = puck.posY - closestY

            val distanceSquared = distanceX * distanceX + distanceY * distanceY

            if (distanceSquared <= (puck.size / 2) * (puck.size / 2) && !collisionCooldown) {
                // Handle collision
                puck(puck, paddle)
                highscore++
                Log.d("HighScore", "Current High Score: $highscore")

                highScoreListener?.onHighScoreUpdated(highscore)

                if (highscore > 1 && highscore % 2 == 0) {
                    puck.speedX *= 1.2f
                    puck.speedY *= 1.2f
                }


                // Update ball position based on collision point
                val angle = Math.atan2(distanceY.toDouble(), distanceX.toDouble())
                val overlap = (puck.size / 2) - sqrt(distanceSquared)
                val newX = puck.posX + (overlap * Math.cos(angle)).toFloat()
                val newY = puck.posY + (overlap * Math.sin(angle)).toFloat()

                puck.posX = newX
                puck.posY = newY

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
        puck.update()
    }

    fun draw() {
        canvas = mHolder!!.lockCanvas()
        canvas.drawColor(Color.BLUE)
        puck.draw(canvas)
        paddle1.speedY = 0f
        paddle1.drawPaddleHockey(canvas)
        paddle2.speedY = 0f
        paddle2.drawPaddleHockey(canvas)
        mHolder!!.unlockCanvasAndPost(canvas)
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
        }
    }
    fun onGameOver() {
        println("onGameOver")
        running = false
        findFragment<PlayHockeyFragment>().makeVisible()

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
