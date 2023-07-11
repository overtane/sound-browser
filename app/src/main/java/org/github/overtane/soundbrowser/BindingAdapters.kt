package org.github.overtane.soundbrowser

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

@BindingAdapter("imageUrl")
fun bindImage(view: ImageView, url: String) {
    if (url.isNotEmpty()) {
        Picasso.get()
            .load(url)
            .error(R.drawable.user_placeholder_error)
            .into(view)
    }
}
