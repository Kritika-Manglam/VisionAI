package com.example.myapplication

import android.content.ContentValues
import android.util.Log
import android.view.MotionEvent
import android.widget.TextView
import com.google.mlkit.common.MlKitException
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.vision.digitalink.DigitalInkRecognition
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModel
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModelIdentifier
import com.google.mlkit.vision.digitalink.DigitalInkRecognizerOptions
import com.google.mlkit.vision.digitalink.Ink
import com.google.mlkit.vision.digitalink.RecognitionResult
import com.google.mlkit.vision.digitalink.*

object StrokeManager {

        private var model: DigitalInkRecognitionModel? = null
        private var inkBuilder = Ink.builder()
        private var strokeBuilder: Ink.Stroke.Builder? = null
        fun addNewTouchEvent(event: MotionEvent) {
            val x = event.x
            val y = event.y
            val t = System.currentTimeMillis()
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    strokeBuilder = Ink.Stroke.builder()
                    strokeBuilder!!.addPoint(Ink.Point.create(x, y, t))
                }
                MotionEvent.ACTION_MOVE -> strokeBuilder!!.addPoint(Ink.Point.create(x, y, t))
                MotionEvent.ACTION_UP -> {
                    strokeBuilder!!.addPoint(Ink.Point.create(x, y, t))
                    inkBuilder.addStroke(strokeBuilder!!.build())
                    strokeBuilder = null
                }
            }
        }

        fun download() {
            var modelIdentifier: DigitalInkRecognitionModelIdentifier? = null
            try {
                modelIdentifier = DigitalInkRecognitionModelIdentifier.fromLanguageTag("tr-TR")
            } catch (e: MlKitException) {
                Log.i(ContentValues.TAG, "Exception$e")
            }
            if (modelIdentifier == null) {
                Log.i(ContentValues.TAG, "Model Bulunamadı")
            }
            model = DigitalInkRecognitionModel.builder(modelIdentifier!!).build()
            val remoteModelManager = RemoteModelManager.getInstance()
            remoteModelManager
                .download(model!!, DownloadConditions.Builder().build())
                .addOnSuccessListener { aVoid: Void? ->
                    Log.i(
                        ContentValues.TAG,
                        "Model downloaded"
                    )
                }
                .addOnFailureListener { e: Exception ->
                    Log.e(
                        ContentValues.TAG,
                        "Error while downloading a model: $e"
                    )
                }
        }

        fun recognize(textView: TextView) {
            val recognizer = DigitalInkRecognition.getClient(
                DigitalInkRecognizerOptions.builder(
                    model!!
                ).build()
            )
            val ink = inkBuilder.build()
            recognizer.recognize(ink)
                .addOnSuccessListener { result: RecognitionResult ->
                    textView.text = result.candidates[0].text
                }
                .addOnFailureListener { e: Exception ->
                    Log.e(
                        ContentValues.TAG,
                        "Error during recognition: $e"
                    )
                }
        }

        fun clear() {
            inkBuilder = Ink.builder()
        }
    }
