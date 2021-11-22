package com.wundermobility.carrental.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.wundermobility.carrental.R

object ImageUtils {

    fun loadImage(context : Context, url : String , imageView: ImageView ){

        Glide
            .with(context)
            .load(url)
            .fitCenter()
            .placeholder(R.drawable.placeholder)
            .into(imageView);
    }

}