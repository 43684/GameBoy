package com.example.gameboy

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.example.gameboy.databinding.FragmentPlayPongBinding

class PlayPongFragment : Fragment(), HighScoreListener, GameViewPong.VisibilityListener {

    lateinit var binding: FragmentPlayPongBinding
    private lateinit var mediaPlayer: MediaPlayer
    var listener: PlayPongFragment.GameListener? = null

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
        binding = FragmentPlayPongBinding.inflate(inflater)
        val gameView = GameViewPong(requireContext())
        gameView.highScoreListener = this
        val rootView = binding.root
        val frameLayout = rootView.findViewById<FrameLayout>(R.id.frame5)

        frameLayout.addView(gameView)
        startMediaPlayer()

        binding.btnPlayAgain.setOnClickListener(View.OnClickListener {
            listener?.startPong()
        } )

        binding.btnMainMenu.setOnClickListener(View.OnClickListener {

            listener?.startPongMenu()


        } )

        return rootView
    }
    fun startMediaPlayer() {
        mediaPlayer = MediaPlayer.create(this@PlayPongFragment.requireContext(), R.raw.mario)
        mediaPlayer.start()
    }
    override fun makeVisible(){
        activity?.runOnUiThread{
        binding.cwGameOver.visibility = View.VISIBLE

        }
        println("Hallo")
    }

    override fun onHighScoreUpdated(highScore: Int) {
        activity?.runOnUiThread {
            binding.textViewPongScore.text = "Score: $highScore"
            binding.tvFinalScore.text ="Final score is: $highScore"
        }
    }

    override fun onDetach(){
        super.onDetach()
        listener = null
    }
    interface GameListener : PlayHockeyFragment.GameListener {
        fun startPong()
        override fun startPongMenu()

    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
        mediaPlayer.release()
    }

}



