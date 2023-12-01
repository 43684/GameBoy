package com.example.gameboy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import com.example.gameboy.databinding.ActivityGameBinding

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

}