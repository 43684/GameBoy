package com.example.gameboy

import android.media.Image.Plane
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.gameboy.databinding.FragmentPongBinding

class PongFragment : Fragment() {
    lateinit var binding: FragmentPongBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Initialize the binding first
        binding = FragmentPongBinding.inflate(layoutInflater)

        // Set an OnClickListener after initializing the binding
        binding.startButton.setOnClickListener(View.OnClickListener {
            // Use childFragmentManager instead of supportFragmentManager if you're working with fragments within a fragment
            childFragmentManager.commit {
                replace(R.id.frame3, PlayPongFragment())
            }
        })

        // Return the root view of the binding
        return binding.root
    }
}