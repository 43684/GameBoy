package com.example.gameboy
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
        binding = FragmentPongBinding.inflate(layoutInflater)

        binding.startButton.setOnClickListener(View.OnClickListener {
            childFragmentManager.commit {
                replace(R.id.frame3, PlayPongFragment())
            }
        })

        return binding.root
    }
}