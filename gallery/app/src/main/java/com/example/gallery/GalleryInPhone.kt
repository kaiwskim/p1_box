package com.example.gallery

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_galleryinphone.*
import java.text.SimpleDateFormat
import java.util.*


class GalleryInPhone : AppCompatActivity() {
    var storage : FirebaseStorage? = null
    var database : FirebaseDatabase? = null
    private var dataReference : DatabaseReference? = null
    private var imageReference : StorageReference? = null
    private var fileRef: StorageReference?= null

    private lateinit var imageView: ImageView
    private lateinit var imageUri: Uri
    private lateinit var currentPhotoPath : String
    private var image_uri: Uri? = null
    private val REQUEST_IMAGE_CAPTURE = 1;
    private val OPEN_GALLERY = 2;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_galleryinphone)
        val view1 = layoutInflater.inflate(R.layout.addphoto_layout,null)
        val bottomSheetDialog = BottomSheetDialog(this)

        storage = FirebaseStorage.getInstance()
        database = FirebaseDatabase.getInstance()

        dataReference = FirebaseDatabase.getInstance().getReference("images")
        imageReference = FirebaseStorage.getInstance().reference.child("images")

        var btn_uploadgallery = view1.findViewById<LinearLayout>(R.id.btn_gallery)
        var btn_uploadcamera = view1.findViewById<LinearLayout>(R.id.btn_camera)

        bottomSheetDialog.setContentView(view1)
        gallery_upload.setOnClickListener {
            bottomSheetDialog.show()
        }

        btn_uploadgallery.setOnClickListener { openGallery() }
        btn_uploadcamera.setOnClickListener {
            takePhoto()
            bottomSheetDialog.dismiss()
        }
        ask_add.setOnClickListener {
            contentUpload()
            bottomSheetDialog.dismiss()
        }
    }
        private fun takePhoto(){

            val values = ContentValues()
            values.put(MediaStore.Images.Media.TITLE, "New Picture")
            values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
            image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE)
        }

        private fun writeNewImageInfoToDB(name: String, url: String){
            val info = UploadInfo(name, url)
            val key = dataReference!!.push().key
            if (key != null) {
                dataReference!!.child(key).setValue(info)
            }
        }

        @SuppressLint("SimpleDateFormat")
        private fun contentUpload() {
            val timestamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val imageFileName = "IMAGE_$timestamp.jpg"
            fileRef = imageReference!!.child(imageFileName)
            fileRef!!.putFile(image_uri!!).addOnSuccessListener { taskSnapshot ->
                val name = taskSnapshot.metadata!!.name
                taskSnapshot.storage.downloadUrl.addOnSuccessListener {task->
                    val url = task.toString()
                    writeNewImageInfoToDB(name!!, url)
                }
                Toast.makeText(this, getString(R.string.upload_success), Toast.LENGTH_LONG).show()
            }
        }

        private fun openGallery(){
            val intent: Intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, OPEN_GALLERY)
        }

        @RequiresApi(Build.VERSION_CODES.P)
        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (resultCode == Activity.RESULT_OK && requestCode == OPEN_GALLERY) {
                image_uri = data?.data
                image_preview.setImageURI(image_uri)
                image_preview.colorFilter = ColorMatrixColorFilter(ColorMatrix().apply { setSaturation(0f)})

            }
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
                image_preview.colorFilter = ColorMatrixColorFilter(ColorMatrix().apply { setSaturation(0f)})
                image_preview.setImageURI(image_uri)

            }
        }
    }
