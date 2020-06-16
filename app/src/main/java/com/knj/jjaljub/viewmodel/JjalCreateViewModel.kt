package com.knj.jjaljub.viewmodel

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.knj.jjaljub.model.Jjal
import com.knj.jjaljub.model.JjalDao
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class JjalCreateViewModel(private val dao: JjalDao) : ViewModel() {
    var tag = MutableLiveData<String>()
    lateinit var intent: Intent
    lateinit var context: Context

    fun onClick(view: View) {
        Log.v(
            "JjalJub",
            "intent uri : " + intent.clipData?.getItemAt(0)?.uri + ", type : " + intent.type
        )

        // Create file name with date and time
        val cal = Calendar.getInstance()
        var fileName = SimpleDateFormat("yyMMdd_HHmmss").format(cal.time)
        val type = intent.type?.split("/")?.get(1)
        fileName = "$fileName.$type"

        val values = ContentValues().apply {
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/jjaljub")
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, intent.type)
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }

        val collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        val item = context.contentResolver.insert(collection, values)!!

        context.contentResolver.openFileDescriptor(item, "w", null).use {
            FileOutputStream(it!!.fileDescriptor).use {
                outputStream ->
                val inputStream = intent.clipData?.getItemAt(0)?.uri?.let { it ->
                    context.contentResolver.openInputStream(
                        it
                    )
                }
                inputStream?.copyTo(outputStream)
                inputStream?.close()
                outputStream.close()
            }
        }

        values.clear()
        values.put(MediaStore.Images.Media.IS_PENDING, 0)
        context.contentResolver.update(item, values, null, null)

        val addRunnable = Runnable {
            // insert into database using Room DAO
            val newJjal = Jjal(
                null,
                item.toString(),
                tag.value.toString()
            )
            dao.insert(newJjal)
            (context as AppCompatActivity).finish()
        }
        val addThread = Thread(addRunnable)
        addThread.start()
    }
}