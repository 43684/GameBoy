package com.example.gameboy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import com.example.gameboy.databinding.ActivityGameBinding
import com.google.firebase.auth.FirebaseAuth

class GameActivity : AppCompatActivity(), PongFragment.GameListener,
    GameSelectFragment.GameListener, Ball.GameListener {

    lateinit var binding: ActivityGameBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGameBinding.inflate(layoutInflater)

        setContentView(binding.root)

        supportFragmentManager.commit {
            replace(R.id.frame3, GameSelectFragment())
        }

        binding.logoutButton.setOnClickListener {
            logoutUser()

            // Optionally, navigate to the login screen or perform other actions
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
    }
