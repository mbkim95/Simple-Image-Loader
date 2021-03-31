package com.example.imageloader

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.ImageLoader

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val imageView = findViewById<ImageView>(R.id.imageView)

        ImageLoader.load(sample2).into(imageView)
    }

    companion object {
        const val sample1 =
            "https://sandbox-api1-kage.kakao.com/dn/O8hgz/bPAbaSZXvet/0Ai6qNUwlEDsKI3bGeH96k/img_110x110.jpg"

        const val sample2 =
            "https://improvephotography.com/wp-content/uploads/2017/07/DSCF5660-Edit-1.jpg"
    }
}