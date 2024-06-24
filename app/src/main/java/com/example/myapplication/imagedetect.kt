package com.example.myapplication



import android.content.Intent
import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.FileUtils
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import com.example.myapplication.ml.AutoModel1
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp

class imagedetect  : AppCompatActivity() {

    val paint = Paint()
    lateinit var btn: Button
    lateinit var imageView: ImageView
    lateinit var bitmap: Bitmap
    var colors = listOf<Int>(Color.BLUE, Color.GREEN, Color.RED, Color.CYAN, Color.GRAY, Color.BLACK,
        Color.DKGRAY, Color.MAGENTA, Color.YELLOW, Color.RED)
    lateinit var labels: List<String>
    lateinit var model: AutoModel1
    val imageProcessor = ImageProcessor.Builder().add(ResizeOp(300, 300, ResizeOp.ResizeMethod.BILINEAR)).build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imagedetect)
       //FileUtil is given to us by tensor flow
        labels = FileUtil.loadLabels(this, "mobilenet_objectdetection_labels.txt")//returns list of all labels
        model = AutoModel1.newInstance(this)

        paint.setColor(Color.BLUE)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 5.0f
//        paint.textSize = paint.textSize*3

        Log.d("labels", labels.toString())

        val intent = Intent()
        intent.setAction(Intent.ACTION_GET_CONTENT)
        intent.setType("image/*")

        btn = findViewById(R.id.btn)
        imageView = findViewById(R.id.gallery)

        btn.setOnClickListener {
            startActivityForResult(intent, 101)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //if the request code matches the code we gave, we will use the data to extract the img
        if(requestCode == 101){
            var uri = data?.data//location where image is stored
            bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)//access the image and store that inside the bitmap
            get_predictions()//we will get the predictions on image after storing the image
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        model.close()
    }

    fun get_predictions(){

        var image = TensorImage.fromBitmap(bitmap)
        image = imageProcessor.process(image)
        val outputs = model.process(image)
        val locations = outputs.locationsAsTensorBuffer.floatArray//to draw location
        val classes = outputs.classesAsTensorBuffer.floatArray//to get the names/labels of object
        val scores = outputs.scoresAsTensorBuffer.floatArray//to get the confidence of the object detection
        val numberOfDetections = outputs.numberOfDetectionsAsTensorBuffer.floatArray



        val mutable = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        var canvas = Canvas(mutable)
        var h = mutable.height
        var w = mutable.width


        paint.textSize = h/20f
        paint.strokeWidth = h/85f
        scores.forEachIndexed { index, fl ->
            if(fl > 0.5){//if score is greater than 50%
                var x = index//index is 0 it will go from 0 to 3 then index is 1 it will go from 4 to 7
                x *= 4
                paint.setColor(colors.get(index))//for every new index we get different color
                paint.style = Paint.Style.STROKE//ti get stroke style rectangle not the filled rectangle
                canvas.drawRect(RectF(locations.get(x+1)*w, locations.get(x)*h, locations.get(x+3)*w, locations.get(x+2)*h), paint)
                paint.style = Paint.Style.FILL//since we want filled text not a stroke text
                canvas.drawText(labels[classes.get(index).toInt()] + " " + fl.toString(), locations.get(x+1)*w, locations.get(x)*h, paint)
            }
        }

        imageView.setImageBitmap(mutable)//to show image on screen

    }
}