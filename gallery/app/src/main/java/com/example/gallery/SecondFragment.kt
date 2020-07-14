//package com.example.gallery
//
//import android.os.Bundle
//import androidx.appcompat.app.AppCompatActivity
//import androidx.viewpager.widget.ViewPager
//
//class SecondFragment : AppCompatActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.main_activity)
//
//        val viewPager = findViewById<ViewPager>(R.id.viewPager)
//        if(viewPager != null) {
//            val adapter = ViewPageAdapter(supportFragmentManager)
//            viewPager.adapter = adapter
//        }
//    }
//}

package com.example.gallery


import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import jp.wasabeef.glide.transformations.GrayscaleTransformation
import kotlinx.android.synthetic.main.activity_main.*

class SecondFragment : Fragment(){
    private var dataReference : DatabaseReference? = null
    private var imageReference : StorageReference? = null
    private var fileRef: StorageReference?= null
    private var mAdapter: FirebaseRecyclerAdapter<UploadInfo, ImgViewHolder>? = null
    private var thisPhoto: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    fun newInstance(): SecondFragment{
        val args = Bundle()
        val frag = SecondFragment()
        frag.arguments = args
        return frag
    }
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
////        var photo_list = arrayListOf<sajins>()
//

//        img3.setOnClickListener{
//            image_view.setImageResource(R.drawable.banana)
//        }
//        img4.setOnClickListener{
//            image_view.setImageResource(R.drawable.blueberry)
//        }
//        img7.setOnClickListener{
//            image_view.setImageResource(R.drawable.grape)
//        }
//        img2.setOnClickListener{
//            image_view.setImageResource(R.drawable.orange)
//        }
//        img5.setOnClickListener{
//            image_view.setImageResource(R.drawable.sigumchi)
//        }
//        img6.setOnClickListener{
//            image_view.setImageResource(R.drawable.strawberry)
//        }
//
//        add_photo.setOnClickListener {
//            val intent = Intent(this, GalleryInPhone::class.java)
//            startActivity(intent)
//        }

    @SuppressLint("ResourceType")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.activity_main, container, false)
        dataReference = FirebaseDatabase.getInstance().getReference("images")
        imageReference = FirebaseStorage.getInstance().reference.child("images")

//
        val addPhotoButton = view.findViewById<Button>(R.id.add_photo)
        val deletePhotoButton = view.findViewById<Button>(R.id.delete_photo)
        val rcPhoto = view?.findViewById<RecyclerView>(R.id.rcvListImg)

        addPhotoButton.setOnClickListener {
            val intent = Intent(this@SecondFragment.context, GalleryInPhone::class.java)
            startActivity(intent)
        }

        deletePhotoButton.setOnClickListener {
            deletePhoto()
        }


        val layoutManager = LinearLayoutManager(this@SecondFragment.context, LinearLayoutManager.HORIZONTAL, false)
        layoutManager.reverseLayout = false
        rcPhoto?.setHasFixedSize(true)
        rcPhoto?.layoutManager = layoutManager
        val query = dataReference!!.limitToLast(100)
        val options = FirebaseRecyclerOptions.Builder<UploadInfo>().setQuery(query, UploadInfo::class.java).setLifecycleOwner(this).build()
//        private var mAdapter: FirebaseRecylcerAdapter<UploadInfo, ImgViewHolder>? = null

        mAdapter = object: FirebaseRecyclerAdapter<UploadInfo, ImgViewHolder>(options){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImgViewHolder {
                return ImgViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)).apply {
                    itemClick = {
                        thisPhoto = it
                        Glide.with(this@SecondFragment).load(it).apply(RequestOptions.bitmapTransform(GrayscaleTransformation())).into(image_view)
                        //Picasso.with(this@SecondFragment.context).load(it).into(image_view)
                        //Toast.makeText(activity, getString(R.string.upload_success), Toast.LENGTH_LONG).show()
                    }
                }
            }
            override fun onBindViewHolder(p0: ImgViewHolder, p1: Int, p2: UploadInfo){
                p0.bind(p2)
            }
        }
        rcPhoto?.adapter = mAdapter

        return view
    }

    private fun deletePhoto() {
        val key: Query
        key = dataReference?.orderByChild("url")?.equalTo(thisPhoto)!!
        key.addListenerForSingleValueEvent ( object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (photoSnapshot: DataSnapshot in snapshot.children){
                    photoSnapshot.ref.removeValue()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG,"onCancelled")
            }

        })
        val storageReference: StorageReference = FirebaseStorage.getInstance().getReferenceFromUrl(thisPhoto!!)
        storageReference.delete().addOnSuccessListener {
            Toast.makeText(activity, "DELETED", Toast.LENGTH_SHORT).show()
        }
        image_view.setImageBitmap(null)

    }


//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        fun showImage(){
//            val imageUri: Uri? = data?.data
//            image_view.setImageURI(imageUri)
//        }
//    }
}

private fun ImageView.setImageResource(it: ImageView) {

}
