package com.example.gameboy

import HighScoreAdapter
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gameboy.databinding.FragmentSelectGameBinding

class GameSelectFragment: Fragment() {



    private val highScores = listOf(
        UserData("Player 1", 100),
        UserData("Player 2", 90),
        UserData("Player 3", 80),
        // Add more high scores as needed
    )

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
    ): View {


        binding = FragmentSelectGameBinding.inflate(layoutInflater)

        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = HighScoreAdapter(highScores)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btn1.setOnClickListener(View.OnClickListener {
            listener?.startPongMenu()
            println("clicking")
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