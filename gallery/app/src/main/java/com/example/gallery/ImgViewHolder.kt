package com.example.gallery

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.squareup.picasso.Picasso
import jp.wasabeef.glide.transformations.GrayscaleTransformation
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_image.view.*

class ImgViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    var itemClick: ((String) -> Unit)? = null
    private var view: View = itemView
    fun bind(p2: UploadInfo?) {
        with(p2!!){
//            Picasso.with(view.context).load(p2.url).into(view.imgView)
            Glide.with(view).load(p2.url).apply(
                RequestOptions.bitmapTransform(
                    GrayscaleTransformation()
                )).into(view.imgView)

            itemView.setOnClickListener{
                itemClick?.invoke(p2.url)
            }
        }

    }
//    init {
//        itemView.setOnClickListener(this)
//    }
//    override fun onClick(p0: View?) {
//        show_image.setImageResource(R.drawable.blueberry)
//    }
}

private fun <P1, R1> (((P1) -> R1)?).invoke(imgView: ImageView?) {
}
