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
import androidx.fragment.app.Fragment
import com.example.gameboy.databinding.FragmentPlayPongBinding

class PlayPongFragment : Fragment(), SurfaceHolder.Callback, View.OnTouchListener {
    lateinit var binding: FragmentPlayPongBinding

    var circlex: Float = 100f
    var circley: Float = 100f
    val ballColor: Paint = Paint()

    private fun drawBall() {
        val canvas: Canvas? = binding.surfaceView.holder.lockCanvas()
        val surfaceBackground = Paint()
        surfaceBackground.color = Color.BLUE

        canvas?.drawRect(
            0f,
            0f,
            binding.surfaceView.width.toFloat(),
            binding.surfaceView.height.toFloat(),
            surfaceBackground
        )

        ballColor.color = Color.RED

        canvas?.drawCircle(
            circlex, circley, 100f, ballColor
        )

        binding.surfaceView.holder.unlockCanvasAndPost(canvas)

        binding.surfaceView.setZOrderOnTop(true)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.surfaceView.holder.addCallback(this)
        binding.surfaceView.setOnTouchListener(this)
        binding = FragmentPlayPongBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        TODO()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        TODO("Not yet implemented")
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        TODO("Not yet implemented")
    }

    override fun onTouch(v: View?, motionEvent: MotionEvent?): Boolean {

        if (view is SurfaceView) {
            val x = motionEvent?.x
            val y = motionEvent?.y

            if (x != null) {
                this.circlex=x
            }
            if (y != null) {
                this.circley=y
            }


            drawBall()
            return true
        } else {
            return false
        }
    }


}