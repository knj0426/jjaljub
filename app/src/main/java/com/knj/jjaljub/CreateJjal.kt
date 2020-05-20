package com.knj.jjaljub

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.room.Room
import com.knj.jjaljub.databinding.ActivityCreateJjalBinding
import com.knj.jjaljub.di.roomModule
import com.knj.jjaljub.model.Jjal
import com.knj.jjaljub.model.JjalDao
import com.knj.jjaljub.model.JjalDatabase
import com.knj.jjaljub.viewmodel.JjalCreateViewModel
import kotlinx.android.synthetic.main.activity_create_jjal.*
import org.koin.android.ext.android.inject
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class CreateJjal : AppCompatActivity() {
    val jjalCreateViewModel: JjalCreateViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        val binding = DataBindingUtil.setContentView<ActivityCreateJjalBinding>(this, R.layout.activity_create_jjal)
        binding.vm = jjalCreateViewModel
        binding.lifecycleOwner = this
        jjalCreateViewModel.context = this
        jjalCreateViewModel.intent = intent

        val imm = applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(tag, 0)

        // Get the URI of shared image
        val uri = intent?.clipData?.getItemAt(0)?.uri

        // update ImageView's URI
        thumbnailView.setImageURI(uri)
    }

    fun onClickCancel(view: View) {
        finish()
    }
}
