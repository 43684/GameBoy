package com.example.gameboy

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.gameboy.databinding.FragmentPongBinding
import com.example.gameboy.databinding.FragmentSelectGameBinding

class GameSelectFragment: Fragment() {

    lateinit var binding: FragmentSelectGameBinding
    var listener: GameListener? = null

    override fun onAttach(context: Context){
        super.onAttach(context)

        try{
            listener = context as GameListener
            println("Successful implementation")
        } catch (e: Exception){
            println("Failed implementation")
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectGameBinding.inflate(layoutInflater)

        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btn1.setOnClickListener(View.OnClickListener {
            listener?.startPongMenu()
        })
    }
    override fun onDetach(){
        super.onDetach()
        listener = null
    }

    interface GameListener{
        fun startPongMenu()

    }
}