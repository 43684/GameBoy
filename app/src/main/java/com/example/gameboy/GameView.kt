package com.example.gameboy

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView

class GameView(context: Context): SurfaceView(context), SurfaceHolder.Callback, Runnable {

    private var thread: Thread? = null
    private var running = false
    lateinit var canvas: Canvas
    private lateinit var ball1: Ball
    private lateinit var ball2: Ball
    var mHolder: SurfaceHolder? = holder

    init {
        if(mHolder != null) {
            mHolder?.addCallback(this)
        }
        setup()
    }

    private fun setup(){
        ball1 = Ball(this.context)
        ball2 = Ball(this.context)
        ball1.posX = 100f
        ball1.posY = 100f
        ball2.posX = 100f
        ball2.posY = 400f
        ball1.paint.color = Color.RED
        ball2.paint.color = Color.GREEN
    }

    fun start(){
        running = true
        thread = Thread(this)
        thread?.start()
    }

    fun stop(){
        running = false
        try{
            thread?.join()
        }catch(e: InterruptedException){
            e.printStackTrace()
        }
    }

    fun update(){
        ball1.update()
        ball2.update()
    }

    fun draw(){
        canvas = mHolder!!.lockCanvas()
        canvas.drawColor(Color.BLUE)
        ball1.draw(canvas)
        ball2.speed = 0f;
        ball2.draw(canvas)
        mHolder!!.unlockCanvasAndPost(canvas)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val sX = event?.x.toString()
        val sY = event?.y.toString()
        ball2.posX = sX.toFloat()
        ball2.posY = sY.toFloat()

        return true
    }
    override fun surfaceCreated(p0: SurfaceHolder) {

    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
        start()
    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
        stop()
    }

    override fun run() {
        while(running){
            update()
            draw()
        }
    }
}