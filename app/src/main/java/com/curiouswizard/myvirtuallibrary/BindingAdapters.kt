package com.curiouswizard.myvirtuallibrary

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

@BindingAdapter("loadBookCoverPhoto")
fun bindBookCoverImage(img: ImageView, imgUrl: String?){
    if (imgUrl.isNullOrEmpty()) {
        Glide.with(img)
            .load(R.drawable.ic_help_24)
            .into(img)
    } else {
        Glide.with(img)
            .load(imgUrl)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_image_animation)
                    .error(R.drawable.ic_broken_image_24)
            )
            .into(img)
    }
}