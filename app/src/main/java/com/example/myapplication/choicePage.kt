package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class choicePage : AppCompatActivity() {
    private lateinit var bt1 : Button
    private lateinit var bt2 : Button
    private lateinit var bt3 : Button
    private lateinit var bt4 : Button
    private lateinit var bt5 : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_choice_page)
        bt1= findViewById(R.id.b1)
        bt1.setOnClickListener{
            val intent= Intent (this , firstpg::class.java )
            startActivity(intent)
        }

        bt2= findViewById(R.id.b2)
        bt2.setOnClickListener{
            val intent= Intent (this , PoseDetection::class.java )
            startActivity(intent)
        }

        bt3= findViewById(R.id.b3)
        bt3.setOnClickListener{
            val intent= Intent (this , IdentifyLanguage::class.java )
            startActivity(intent)
        }

        bt4= findViewById(R.id.b4)
        bt4.setOnClickListener{
            val intent= Intent (this ,RecognizeText::class.java )
            startActivity(intent)
        }

        bt5= findViewById(R.id.b5)
        bt5.setOnClickListener{
            val intent= Intent (this ,ReplyBot::class.java )
            startActivity(intent)
        }
    }
}