package com.example.soundbrowser

import android.widget.ImageView
import android.widget.TextView
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

@BindingAdapter("samplerate")
fun bindSampleRate(view: TextView, samplerate: Double) {
    val str = samplerate.toInt().toString() + " Hz"
    view.text = str
}

@BindingAdapter("duration")
fun bindDuration(view: TextView, duration: Double) {
    val str = duration.toInt().toString() + " s"
    view.text = str
}

@BindingAdapter("integer")
fun bindInteger(view: TextView, value: Int) {
    view.text = value.toString()
}