package com.example.gameboy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.commit
import com.example.gameboy.databinding.ActivityGameBinding
import com.example.gameboy.databinding.ActivityMainBinding

class GameActivity : AppCompatActivity() {

    lateinit var binding: ActivityGameBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGameBinding.inflate(layoutInflater) // Replace YourBindingClass with your actual binding class

        setContentView(binding.root)

        binding.knapp.setOnClickListener(View.OnClickListener {
            supportFragmentManager.commit {
                replace(R.id.frame3, PongFragment())
            }
        })
        binding.knapp2.setOnClickListener(View.OnClickListener {
            supportFragmentManager.commit {
                replace(R.id.frame3, HockeyFragment())
            }
        })
    }
}