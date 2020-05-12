package com.knj.jjaljub

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StableIdKeyProvider
import androidx.recyclerview.selection.StorageStrategy
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_create_jjal.*
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.util.jar.Manifest

class MainActivity : Activity() {
    var isUpload = false
    val REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 1
    var jjalDb : JjalDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        jjalDb = Room.databaseBuilder(
            applicationContext,
            JjalDatabase::class.java, "jjal.db"
        ).build()

        search_bar.setOnEditorActionListener { v, actionId, event ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    Log.d("JjalJub", "IME_ACTION_SEARCH ")
                    val adaptor : MainRvAdaptor = mRecyclerView.adapter as MainRvAdaptor
                    adaptor.filter.filter(v.text)
                }
            }
            true
        }
        updateJjalList()

    }

    override fun onResume() {
        super.onResume()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                } else {
                    requestPermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
                }
            }
        }

        updateJjalList()
    }

    fun handleUpload(uri : Uri) {
        var result = Intent()
        result.setData(uri)
        setResult(Activity.RESULT_OK, result)
        Log.d("JjalJub", "path = ${uri.toString()}")
        finish()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_WRITE_EXTERNAL_STORAGE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        }
    }

    fun updateJjalList() {
        val runnable = Runnable {
            try {
                val jjalList = jjalDb?.jjalDao()?.getAll()!!
                val jjalArray = ArrayList<Jjal>()
                jjalArray.addAll(jjalList)
                val mAdaptor = MainRvAdaptor(this, jjalArray)
                mAdaptor.notifyDataSetChanged()
                mRecyclerView.adapter = mAdaptor

                val lm = androidx.recyclerview.widget.GridLayoutManager(this, 4)
                mRecyclerView.layoutManager = lm
                mRecyclerView.setHasFixedSize(true)

                if (intent?.action.toString() == "com.sec.android.app.myfiles.PICK_DATA" ||
                        intent?.action == Intent.ACTION_GET_CONTENT) {
                    isUpload = true
                }
            } catch (e : Exception) {
                Log.d("JjalJub", "Error - $e")
            }
        }

        val thread = Thread(runnable)
        thread.start()
    }
}
