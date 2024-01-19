package com.example.gameboy

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

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

            highScoreListener?.onHighScoreUpdated(highscore)

            if (highscore >= 0) {

                speedX *= 1.2f
                speedY *= 1.2f
            }
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
                updateHighScore(highscore)
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

    fun updateHighScore(newHighScore: Int) {
        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        val database: DatabaseReference = FirebaseDatabase.getInstance().reference
        // Get the current user's UID from FirebaseAuth
        val uid: String? = auth.currentUser?.uid

        if (uid != null) {
            // Reference to the user's data in the Firebase database
            val userRef: DatabaseReference = database.child("users").child(uid)

            // Read the current high score from the database
            userRef.child("hockeyScore").addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val currentHighScore = dataSnapshot.getValue(Int::class.java)

                    // Check if the new high score is higher than the existing one
                    if (currentHighScore == null || newHighScore > currentHighScore) {
                        // Update the high score in the database
                        userRef.child("hockeyScore").setValue(newHighScore)
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