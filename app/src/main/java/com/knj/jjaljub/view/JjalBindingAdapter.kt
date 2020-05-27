package com.knj.jjaljub.view

import android.net.Uri
import android.util.Log
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.knj.jjaljub.model.Jjal

@BindingAdapter("imageUri")
fun setImageUri(view: ImageView, uri: String) {
    view.setImageURI(Uri.parse(uri))
}

interface CheckedChangedListener {
    fun onCheckedChanged(isChecked: Boolean)
}

@BindingAdapter("onCheckedChanged")
fun setOnCheckedChanged(view: CompoundButton, listener: CheckedChangedListener?) {
    if (listener == null) {
        view.setOnCheckedChangeListener(null)
    } else {
        view.setOnCheckedChangeListener { view, isChecked ->
            listener.onCheckedChanged(isChecked)
        }
    }
}