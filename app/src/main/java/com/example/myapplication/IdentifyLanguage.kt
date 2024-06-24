package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
import com.google.firebase.ml.naturallanguage.languageid.internal.LanguageIdentificationJni
import org.intellij.lang.annotations.Language

class IdentifyLanguage : AppCompatActivity() {
    lateinit var etlangtext:EditText
    lateinit var btnCheckNow:Button
    lateinit var tvResult:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_identify_language)
        etlangtext=findViewById(R.id.editTextText)
        btnCheckNow=findViewById(R.id.button)
        tvResult=findViewById(R.id.textView4)
        btnCheckNow.setOnClickListener{
            val langText:String=etlangtext.text.toString()
            if(langText.equals(""))
            {
                Toast.makeText(this@IdentifyLanguage, " Please enter text",Toast.LENGTH_LONG).show()
            }
            else
            {
                detectLang(langText)
            }
        }
    }

    private fun detectLang(langText: String) {
       // val languageIdentifier :
        val languageIdentifier = FirebaseNaturalLanguage.getInstance().languageIdentification

        languageIdentifier.identifyLanguage(langText)
            .addOnSuccessListener { languageCode ->
                if (languageCode != "und") {
                    tvResult.text= "Language: $languageCode";
                } else {
                    tvResult.text="Can't identify language";
                }
            }
            .addOnFailureListener {
                tvResult.text="Exception${it.message}"
            }

    }
}