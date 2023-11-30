package com.example.gameboy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.commit
import com.example.gameboy.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

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
}