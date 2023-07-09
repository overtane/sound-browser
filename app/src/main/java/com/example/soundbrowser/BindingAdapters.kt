package com.example.soundbrowser

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

@BindingAdapter("imageUrl")
fun bindImage(view: ImageView, url: String) {
    Picasso.get()
        .load(url)
        .into(view)
    // TODO add default and error images
        //.placeholder(R.drawable.)
}
