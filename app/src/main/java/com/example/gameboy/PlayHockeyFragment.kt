package com.example.gameboy

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.example.gameboy.databinding.FragmentPlayHockeyBinding

class PlayHockeyFragment : Fragment(), HighScoreListener, GameViewPong.VisibilityListener {

    lateinit var binding: FragmentPlayHockeyBinding
    var listener: PlayHockeyFragment.GameListener? = null

    override fun onAttach(context: Context){
        super.onAttach(context)

        try{
            listener = context as PlayPongFragment.GameListener
            println("Successful implementation in play pong fragment")
        } catch (e: Exception){
            println("Failed implementation")
        }

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayHockeyBinding.inflate(inflater)
        val gameView = GameViewHockey(requireContext(),this)
        gameView.highScoreListener = this
        val rootView = binding.root
        val frameLayout = rootView.findViewById<FrameLayout>(R.id.frame5)

        frameLayout.addView(gameView)

        binding.btnMainMenu.setOnClickListener(View.OnClickListener {
            listener?.startPongMenu()
        })

        binding.btnPlayAgain.setOnClickListener(View.OnClickListener {
            listener?.startHockey()
        })

        return rootView
    }

    override fun onHighScoreUpdated(highScore: Int) {
        println("highscore updated, fragment")
        activity?.runOnUiThread {
            binding.textViewPongScore.text = "Score: $highScore"
            binding.tvFinalScore.text ="Final score is: $highScore"
            println("fragment log score")
        }
    }

    override fun onDetach(){
        super.onDetach()
        listener = null
    }
    override fun makeVisible(){
        activity?.runOnUiThread{
            binding.cwGameOver1.visibility = View.VISIBLE

        }
        println("Hallo")
    }
    interface GameListener{
        fun startHockey()
        fun startPongMenu()

    }
}
