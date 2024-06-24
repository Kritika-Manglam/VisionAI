package com.example.myapplication

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.tasks.Task
import com.google.android.gms.vision.text.Text
import com.google.android.gms.vision.text.TextRecognizer
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.mlkit.vision.common.InputImage
import java.io.IOException

class RecognizeText : AppCompatActivity() {
    lateinit var tvResult:TextView
    lateinit var btnChoice:Button
    var intentActivityResultLauncher: ActivityResultLauncher<Intent>?=null

val detector = FirebaseVision.getInstance().onDeviceTextRecognizer
private val STORAGE_PERMISSION_CODE=113
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recognize_text)

        tvResult=findViewById(R.id.tvresult)
        btnChoice=findViewById(R.id.btnChoice)
       //textRecognizer=TextRecognizer
        intentActivityResultLauncher=registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(), ActivityResultCallback {
                //here we haandle result
                val data=it.data
                val imageUri=data?.data


                convertImageToText(imageUri)
            }
        )
        btnChoice.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_MEDIA_IMAGES
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                    STORAGE_PERMISSION_CODE
                )
            } else {
                chooseImage()
            }
        }
    }

    private fun chooseImage() {
        val chooseIntent = Intent()
        chooseIntent.type = "image/*"
        chooseIntent.action = Intent.ACTION_GET_CONTENT
        intentActivityResultLauncher?.launch(chooseIntent)
    }

    private fun convertImageToText(imageUri: Uri?) {
        val inputImage: FirebaseVisionImage
        try {
            imageUri?.let {
            //prepare the input image
            inputImage = FirebaseVisionImage.fromFilePath(applicationContext, it)
            //get text from input image
            val result = detector.processImage(inputImage)
                .addOnSuccessListener {
                    // Task completed successfully
                    tvResult.text=it.text
                }
                .addOnFailureListener {
                    // Task failed with an exception
                    tvResult.text="Error:${it.message}"
                }
            } ?: run {
                tvResult.text = "Error: imageUri is null"
            }
        } catch (e: Exception) {

        }
    }
    override fun onResume()
    {
        super.onResume()
        checkPermission(Manifest.permission.READ_MEDIA_IMAGES,STORAGE_PERMISSION_CODE)
    }
    private fun checkPermission(permission:String, requestCode:Int) {
        if (ContextCompat.checkSelfPermission(this@RecognizeText, permission) == PackageManager.PERMISSION_DENIED)
        //take permission
            ActivityCompat.requestPermissions(this@RecognizeText, arrayOf(permission), requestCode)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode , permissions, grantResults)
        if(requestCode==STORAGE_PERMISSION_CODE)
        {
            if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this@RecognizeText,"storage permission granted", Toast.LENGTH_SHORT).show()
            }else
            {
                Toast.makeText(this@RecognizeText,"storage permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}