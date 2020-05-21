package com.knj.jjaljub.view

import android.net.Uri
import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("imageUri")
fun setImageUri(view: ImageView, uri: String) {
    view.setImageURI(Uri.parse(uri))
}