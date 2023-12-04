package com.example.gameboy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.commit
import com.example.gameboy.databinding.ActivityMainBinding

import com.google.firebase.auth.FirebaseAuth

import com.example.gameboy.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.logoutButton.setOnClickListener {
            logoutUser()

            // Optionally, navigate to the login screen or perform other actions
        }
        
        binding.button5.setOnClickListener(View.OnClickListener {
            intent = Intent(
                this, GameActivity::class.java

            )
            startActivity(intent)
        })

        supportFragmentManager.commit {
            add(R.id.frame, PongFragment())
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