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

class PlayPongFragment : Fragment() {
    lateinit var binding: FragmentPlayPongBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the binding layout
        binding = FragmentPlayPongBinding.inflate(inflater)

        // Create an instance of your custom GameView
        val gameView = GameView(requireContext())

        // Set the GameView as the content view for the fragment
        return gameView
    }
}