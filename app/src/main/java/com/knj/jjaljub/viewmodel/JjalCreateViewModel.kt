package com.knj.jjaljub.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
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
            "intent uri : " + intent.clipData.getItemAt(0).uri + ", type : " + intent.type
        )

        // Store image file on SD card
        val filePath = "/sdcard/jjaljub"
        val fileDescriptor = File(filePath)
        // Make directory
        if (!fileDescriptor.exists()) {
            val result = fileDescriptor.mkdirs()
            Log.v("JjalJub", "mkdir result = $result")

        }

        // Create file name with date and time
        val cal = Calendar.getInstance()
        val fileName = SimpleDateFormat("yyMMdd_HHmmss").format(cal.time)

        val type = intent.type.split("/")[1]
        val fullPath = "$filePath/$fileName.$type"

        // Setting file path
        val outputFile = File(fullPath)
        val outputStream = FileOutputStream(outputFile)

        context.contentResolver.openInputStream(intent.clipData.getItemAt(0).uri)
            .copyTo(outputStream)
        val fileUri = Uri.parse("file://$fullPath")

        val addRunnable = Runnable {
            // insert into database using Room DAO
            val newJjal = Jjal(
                null,
                fileUri.toString(),
                tag.value.toString()
            )
            dao.insert(newJjal)
            (context as AppCompatActivity).finish()
        }
        val addThread = Thread(addRunnable)
        addThread.start()
    }
}