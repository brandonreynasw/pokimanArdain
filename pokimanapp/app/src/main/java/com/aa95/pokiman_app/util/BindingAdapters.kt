package com.aa95.pokiman_app.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.aa95.pokiman_app.R
import com.bumptech.glide.Glide

object BindingAdapters {

    @BindingAdapter("imageUrl")
    @JvmStatic
    fun setImageUrl(imageView: ImageView, url: String?) {
        if (url != null) {
            Glide.with(imageView.context)
                .load(url)
                .placeholder(R.drawable.hp_bar)
                .into(imageView)
        }
    }

}