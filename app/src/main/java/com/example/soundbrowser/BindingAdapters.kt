package com.example.soundbrowser

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

@BindingAdapter("imageUrl")
fun bindImage(view: ImageView, url: String) {
    Picasso.get()
        .load(url)
        .into(view)
        //.placeholder(R.drawable.)
}
