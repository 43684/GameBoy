package com.example.gameboy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.example.gameboy.databinding.FragmentPlayHockeyBinding

class PlayHockeyFragment : Fragment() {

    lateinit var binding: FragmentPlayHockeyBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayHockeyBinding.inflate(inflater)
        val gameView = GameViewHockey(requireContext())
        val rootView = binding.root
        val frameLayout = rootView.findViewById<FrameLayout>(R.id.frame5)

        frameLayout.addView(gameView)

        return rootView
    }
}
