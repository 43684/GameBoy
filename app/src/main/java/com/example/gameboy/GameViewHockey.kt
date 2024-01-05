package com.example.gameboy

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.view.SurfaceHolder
import android.view.SurfaceView

class GameViewHockey (context: Context) : SurfaceView(context), SurfaceHolder.Callback, Runnable {

    private var thread : Thread? = null
    private var running = false
    lateinit var canvas: Canvas
    private lateinit var puck: Puck
    var mHolder : SurfaceHolder? = holder
    init {
        if (mHolder != null){
            mHolder?.addCallback(this)
        }
        setup()
    }

    fun start(){
        running = true
        thread = Thread(this)
        thread?.start()
    }

    fun stop(){
        running = false
        try {
            thread?.join()
        } catch (e:InterruptedException){
            e.printStackTrace()
        }
    }

    fun update(){
        puck.update()
    }



    private fun setup(){
        puck = Puck(this.context, 500f, 500f, 50f, -5f, 5f)
        puck.paint.color = Color.RED
    }
    override fun surfaceCreated(holder: SurfaceHolder) {
        TODO("Not yet implemented")
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        TODO("Not yet implemented")
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        TODO("Not yet implemented")
    }

    override fun run() {
        TODO("Not yet implemented")
    }


}