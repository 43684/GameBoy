package com.example.gameboy

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.example.gameboy.databinding.FragmentPlayPongBinding
import com.example.gameboy.databinding.FragmentPongBinding
class PlayPongFragment : Fragment(), HighScoreListener {

    lateinit var binding: FragmentPlayPongBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlayPongBinding.inflate(inflater)

        val gameView = GameView(requireContext())
        gameView.highScoreListener = this

        val rootView = inflater.inflate(R.layout.fragment_play_pong, container, false)
        val frameLayout = rootView.findViewById<FrameLayout>(R.id.frame5)

        frameLayout.addView(gameView)

        return rootView
    }

    // Implement the HighScoreListener interface
    override fun onHighScoreUpdated(highScore: Int) {
        binding.textViewPongScore.text = "Score: $highScore"
    }
}