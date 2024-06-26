package com.example.myapplication

import android.app.Instrumentation.ActivityResult
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Locale


class SpeechToText : AppCompatActivity(), TextToSpeech.OnInitListener{

    lateinit var bt : Button
    lateinit var txt: EditText
    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    lateinit var bt1 : Button

    lateinit var textToSpeech: TextToSpeech
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_speech_to_text)
        txt=findViewById(R.id.txtSpeechData)
        bt= findViewById(R.id.btnSpeech)
        bt1=findViewById(R.id.btnTextToSpeech)

        bt1.isEnabled=false
        textToSpeech= TextToSpeech(this,this)
        bt1.setOnClickListener {
            speakOut()
        }


        bt.setOnClickListener{
           val intent= Intent (RecognizerIntent.ACTION_RECOGNIZE_SPEECH )
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Say Something")

            try {
                activityResultLauncher.launch(intent)
            }catch(exp:ActivityNotFoundException)
            {
                 Toast.makeText(applicationContext,"Device not supported",Toast.LENGTH_SHORT).show()
            }
        }
        activityResultLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result: androidx.activity.result.ActivityResult ->
            if(result!!.resultCode== RESULT_OK && result!!.data!=null)
            {
                val speechtext=result!!.data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)as ArrayList<Editable>
                txt.text=speechtext[0]
            }
        }

    }

    private fun speakOut()
    {
        val textForSpeech=txt.text.toString()
        textToSpeech.speak(textForSpeech, TextToSpeech.QUEUE_FLUSH, null, null )
    }

    override fun onInit(status: Int)
    {
        if(status==TextToSpeech.SUCCESS)
        {
            val res=textToSpeech.setLanguage(Locale.getDefault())
            if(res==TextToSpeech.LANG_MISSING_DATA || res==TextToSpeech.LANG_NOT_SUPPORTED)
            {
                Toast.makeText(applicationContext,"Language not supported", Toast.LENGTH_SHORT).show()

            }
            else{
                bt1.isEnabled=true

            }
        }else
        {
            Toast.makeText(applicationContext,"failed to initialised", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        textToSpeech.stop()
        textToSpeech.shutdown()
        super.onDestroy()
    }
}