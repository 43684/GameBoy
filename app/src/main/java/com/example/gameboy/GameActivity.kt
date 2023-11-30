package com.example.gameboy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.commit
import com.example.gameboy.databinding.ActivityGameBinding
import com.example.gameboy.databinding.ActivityMainBinding

class GameActivity : AppCompatActivity(), PongFragment.GameListener, GameSelectFragment.GameListener {

    lateinit var binding: ActivityGameBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGameBinding.inflate(layoutInflater) // Replace YourBindingClass with your actual binding class

        setContentView(binding.root)


            supportFragmentManager.commit {
                replace(R.id.frame3, GameSelectFragment())
            }

    }

    override fun startPong(){
        supportFragmentManager.commit {
            replace(R.id.frame3, PlayPongFragment())
        }
    }
    override fun startPongMenu(){
        supportFragmentManager.commit {
            replace(R.id.frame3, PongFragment())
        }
    }

}