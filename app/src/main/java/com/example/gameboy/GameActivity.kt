package com.example.gameboy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import com.example.gameboy.databinding.ActivityGameBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class GameActivity : AppCompatActivity(), PongFragment.GameListener,
    GameSelectFragment.GameListener {

    lateinit var binding: ActivityGameBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGameBinding.inflate(layoutInflater)

        setContentView(binding.root)

        supportFragmentManager.commit {
            replace(R.id.frame3, GameSelectFragment())
        }

    }

    override fun startPong() {
        supportFragmentManager.commit {
            replace(R.id.frame3, PlayPongFragment())
        }
    }

    override fun startPongMenu() {
        supportFragmentManager.commit {
            replace(R.id.frame3, PongFragment())
        }
    }

    fun logoutUser() {
        val auth = FirebaseAuth.getInstance()
        auth.signOut()
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        finish()
    }

    data class UserData(val name: String, val highscore: Int)

    fun fetchData() {
        val database = FirebaseDatabase.getInstance()
        val dataRef: DatabaseReference = database.getReference("users")

        dataRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userList = mutableListOf<UserData>()

                for (childSnapshot in snapshot.children) {
                    val userDataMap = childSnapshot.value as? Map<String, Any>

                    if (userDataMap != null) {
                        val name = userDataMap["name"] as? String ?: ""
                        val highscore = (userDataMap["highscore"] as? Long)?.toInt() ?: 0

                        val userData = UserData(name, highscore)
                        userList.add(userData)
                    }
                }


                processUserData(userList)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors
                println("Error: $error")
            }
        })
    }

    fun processUserData(userList: List<UserData>) {
        // Do something with the user list
        userList.forEach { userData ->
            println("Name: ${userData.name}, Highscore: ${userData.highscore}")
        }
    }

    fun updateUserData(newHighscore: Int) {
        var firebaseAuth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance()
        val dataRef: DatabaseReference = database.getReference("users").child(firebaseAuth.currentUser!!.uid).child("highscore")

        // Update the data in the database
        dataRef.setValue(newHighscore)
            .addOnSuccessListener {
                // Highscore updated successfully
                println("Highscore updated successfully")
            }
            .addOnFailureListener { error ->
                // Handle errors
                println("Error updating highscore: $error")
            }
    }

}