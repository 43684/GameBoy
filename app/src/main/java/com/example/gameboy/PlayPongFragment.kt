package com.example.gameboy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.example.gameboy.databinding.FragmentPlayPongBinding

class PlayPongFragment : Fragment(), HighScoreListener {

    lateinit var binding: FragmentPlayPongBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayPongBinding.inflate(inflater)

        val gameView = GameView(requireContext())
        gameView.highScoreListener = this

        val rootView = binding.root
        val frameLayout = rootView.findViewById<FrameLayout>(R.id.frame5)

        frameLayout.addView(gameView)

        return rootView
    }

    /***
     * Här nedan
     */
    override fun onHighScoreUpdated(highScore: Int) {
        activity?.runOnUiThread {
            binding.textViewPongScore.text = "Score: $highScore"
        }
    }
}