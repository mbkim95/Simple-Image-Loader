package com.example.imageloader

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.ImageLoader

class MainActivity : AppCompatActivity() {
    var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val imageView = findViewById<ImageView>(R.id.imageView)
        val button = findViewById<Button>(R.id.button)

        button.setOnClickListener {
            if (count++ % 2 == 0) {
                ImageLoader.get(this).load(sample1).toBitmap {
                    imageView.setImageBitmap(it)
                }
            } else {
                ImageLoader.get(this).load(sample2).placeHolder(R.drawable.ic_launcher_foreground)
                    .into(imageView)
            }
        }
    }

    companion object {
        const val sample1 =
            "https://sandbox-api1-kage.kakao.com/dn/O8hgz/bPAbaSZXvet/0Ai6qNUwlEDsKI3bGeH96k/img_110x110.jpg"

        const val sample2 =
            "https://improvephotography.com/wp-content/uploads/2017/07/DSCF5660-Edit-1.jpg"
    }
}