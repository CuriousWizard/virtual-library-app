package com.curiouswizard.myvirtuallibrary

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

object BindingAdapters{

    @BindingAdapter("loadBookCoverPhoto")
    @JvmStatic
    fun bindBookCoverImage(img: ImageView, imgUrl: String?){
        if (imgUrl != null) {
            if (imgUrl.isNotEmpty()) {
                Glide.with(img)
                    .load(imgUrl)
                    .apply(
                        RequestOptions()
                            .placeholder(R.drawable.loading_image_animation)
                            .error(R.drawable.ic_broken_image_24)
                            .fitCenter()
                    )
                    .into(img)
            }
        } else {
            Glide.with(img)
                .load(R.drawable.ic_unknown_book)
                .into(img)
        }
    }

    /**
     * Use this binding adapter to show and hide the views using boolean variables
     */
    @BindingAdapter("fadeImgVisible")
    @JvmStatic
    fun setFadeImgVisible(img: ImageView, visible: Boolean? = true) {
        if (img.tag == null) {
            img.tag = true
            img.visibility = if (visible == true) View.VISIBLE else View.GONE
        } else {
            img.animate().cancel()
            if (visible == true) {
                if (img.visibility == View.GONE)
                    img.clearFocus()
                    img.fadeIn()
            } else {
                if (img.visibility == View.VISIBLE){
                    img.clearFocus()
                    img.fadeOut()
                }

            }
        }
    }

    /**
     * Use this binding adapter to show and hide the views using boolean variables
     */
    @BindingAdapter("fadeVisible")
    @JvmStatic
    fun setFadeVisible(view: View, visible: Boolean? = true) {
        if (view.tag == null) {
            view.tag = true
            view.visibility = if (visible == true) View.VISIBLE else View.GONE
        } else {
            view.animate().cancel()
            if (visible == true) {
                if (view.visibility == View.GONE)
                    view.fadeIn()
            } else {
                if (view.visibility == View.VISIBLE)
                    view.fadeOut()
            }
        }
    }
}