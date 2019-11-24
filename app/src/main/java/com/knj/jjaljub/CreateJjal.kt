package com.knj.jjaljub

import android.app.Activity
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_create_jjal.*
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class CreateJjal : Activity() {
    var mPath : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_jjal)

        Realm.init(this)



        val uri = intent.clipData.getItemAt(0).uri
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

        contentResolver.openInputStream(uri).copyTo(outputStream)
        val fileUri = Uri.parse("file://$fullPath")
        thumbnailView.setImageURI(fileUri)

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
            realm.copyToRealm(jjal)
        }
    }
}
