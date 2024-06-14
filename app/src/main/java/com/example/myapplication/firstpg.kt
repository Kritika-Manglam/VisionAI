package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class firstpg : AppCompatActivity() {
    private lateinit var but1 : Button
    private lateinit var but2 : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_firstpg)
        but1= findViewById(R.id.button1)
        but1.setOnClickListener{
            val intent= Intent (this , MainActivity::class.java )
            startActivity(intent)
        }

        but2= findViewById(R.id.button2)
        but2.setOnClickListener{
            val intent= Intent (this , imagedetect::class.java )
            startActivity(intent)
        }
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
    }
}