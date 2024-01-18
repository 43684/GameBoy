package com.example.gameboy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.commit
import com.example.gameboy.databinding.ActivityGameBinding
import com.google.firebase.auth.FirebaseAuth

class GameActivity : AppCompatActivity(), PlayPongFragment.GameListener,PlayHockeyFragment.GameListener,
    GameSelectFragment.GameListener {

    var hasNoUser = false

    lateinit var binding: ActivityGameBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGameBinding.inflate(layoutInflater)


        setContentView(binding.root)

        hasNoUser = (FirebaseAuth.getInstance().currentUser == null)

        if (hasNoUser) {
            binding.logoutButton.setText("Turn Back")
        }

        supportFragmentManager.commit {
            replace(R.id.frame3, GameSelectFragment())
        }

        binding.logoutButton.setOnClickListener {
            logoutUser()
        }
    }

    override fun startPong() {
        supportFragmentManager.commit {
            replace(R.id.frame3, PlayPongFragment())
        }

    }

    override fun startPongMenu() {
        supportFragmentManager.commit {
            replace(R.id.frame3, GameSelectFragment())
        }
    }

    override fun startHockey() {
        supportFragmentManager.commit {
            replace(R.id.frame3, PlayHockeyFragment())
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
