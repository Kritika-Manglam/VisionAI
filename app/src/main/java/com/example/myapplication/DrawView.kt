package com.example.myapplication

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.graphics.*



class DrawView(context: Context?, attrs: AttributeSet?) :
        View(context, attrs) {
        private val currentStrokePaint: Paint = Paint()
        private val canvasPaint: Paint
        private val currentStroke: Path
        private var drawCanvas: Canvas? = null
        private var canvasBitmap: Bitmap? = null
        private val STROKE_WIDTH_DP = 8.0f

        init {
            currentStrokePaint.color = Color.BLACK
            currentStrokePaint.isAntiAlias = true
            currentStrokePaint.strokeWidth = STROKE_WIDTH_DP
            currentStrokePaint.style = Paint.Style.STROKE
            currentStrokePaint.strokeJoin = Paint.Join.ROUND
            currentStrokePaint.strokeCap = Paint.Cap.ROUND
            currentStroke = Path()
            canvasPaint = Paint(Paint.DITHER_FLAG)
        }

        override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
            canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            drawCanvas = Canvas(canvasBitmap!!)
            invalidate()
        }

        override fun onDraw(canvas: Canvas) {
            canvas.drawBitmap(canvasBitmap!!, 0f, 0f, canvasPaint)
            canvas.drawPath(currentStroke, currentStrokePaint)
        }

        override fun onTouchEvent(event: MotionEvent): Boolean {
            val action = event.actionMasked
            val x = event.x
            val y = event.y
            when (action) {
                MotionEvent.ACTION_DOWN -> currentStroke.moveTo(x, y)
                MotionEvent.ACTION_MOVE -> currentStroke.lineTo(x, y)
                MotionEvent.ACTION_UP -> {
                    currentStroke.lineTo(x, y)
                    drawCanvas!!.drawPath(currentStroke, currentStrokePaint)
                    currentStroke.reset()
                }
                else -> {}
            }
            StrokeManager.addNewTouchEvent(event)
            invalidate()
            return true
        }

        fun clear() {
            onSizeChanged(
                canvasBitmap!!.width,
                canvasBitmap!!.height,
                canvasBitmap!!.width,
                canvasBitmap!!.height
            )
        }
    }
