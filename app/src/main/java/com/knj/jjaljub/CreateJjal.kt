package com.knj.jjaljub

import android.app.Activity
import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_create_jjal.*
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class CreateJjal : Activity() {
    var mUri : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_create_jjal)
        tag.requestFocus()
        val imm = applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(tag, 0)


        // Initiate Realm instance
        Realm.init(this)

        // Get the URI of shared image
        if (intent != null && intent.clipData != null) {
            mUri = intent.clipData.getItemAt(0).uri
        }

        // update ImageView's URI
        thumbnailView.setImageURI(mUri)
    }

    fun onClickCancel(view: View) {
        finish()
    }

    fun onClickOk(view: View) {
        Log.v("JjalJub", "intent uri : " + intent.clipData.getItemAt(0).uri + ", type : " + intent.type)

        val filePath : String = "/sdcard/jjaljub"
        val fileDescriptor = File(filePath)
        if (!fileDescriptor.exists()) {
            val result = fileDescriptor.mkdirs()
            Log.v("JjalJub", "mkdir result = $result")

        }
        val cal = Calendar.getInstance();
        val fileName = SimpleDateFormat("yyMMdd_HHmmss").format(cal.time)

        val type  = intent.type.split("/")[1]
        val fullPath = "$filePath/$fileName.$type"

        val outputFile = File(fullPath)
        val outputStream = FileOutputStream(outputFile)

        contentResolver.openInputStream(mUri).copyTo(outputStream)
        val fileUri = Uri.parse("file://$fullPath")

        val defaultRealm = Realm.getDefaultInstance()

        defaultRealm.executeTransaction { realm ->
            val jjal = Jjal()
            val nextId = realm.where<Jjal>().max("id")
            val id = if (nextId == null) {
                0
            } else {
                nextId.toInt() + 1
            }
            jjal.id = id
            jjal.path = fileUri.toString()
            jjal.tag = tag.text.toString()
            realm.copyToRealm(jjal)
            finish()
        }
    }
}
