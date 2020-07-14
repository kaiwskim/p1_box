package com.example.gallery

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.storage.FirebaseStorage

class Uploadfromgallery : AppCompatActivity() {
    var PICK_IMAGE_FROM_GALLERY = 0
    var storage: FirebaseStorage? = null
    var photoUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_uploadfromgallery)
    }
}