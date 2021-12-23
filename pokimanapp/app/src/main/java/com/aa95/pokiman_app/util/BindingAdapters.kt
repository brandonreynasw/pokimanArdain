package com.aa95.pokiman_app.util

import android.widget.ImageView
import android.widget.TextView
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
    @BindingAdapter("currentHp", "maxHp")
    @JvmStatic
    fun setHpCount(textView: TextView, currentHp: String?, maxHp: String?) {
        if (currentHp != null && maxHp != null) {
            textView.text = textView.context.getString(R.string.hp_stats, currentHp, maxHp)
        }
    }

}