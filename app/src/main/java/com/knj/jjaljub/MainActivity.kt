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
import android.widget.Toast
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StableIdKeyProvider
import androidx.recyclerview.selection.StorageStrategy
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_create_jjal.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.jar.Manifest

class MainActivity : Activity() {
    var isUpload = false
    val REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
        Realm.init(this)
        val defaultRealm = Realm.getDefaultInstance()
        defaultRealm.executeTransaction { realm ->
            val jjalList = ArrayList<Jjal>()
            for (jjal : Jjal in realm.where(Jjal::class.java).findAll()) {
                jjalList.add(jjal)
            }
            val mAdaptor = MainRvAdaptor(this, jjalList)
            mRecyclerView.adapter = mAdaptor

            val lm = androidx.recyclerview.widget.GridLayoutManager(this, 4)
            mRecyclerView.layoutManager = lm
            mRecyclerView.setHasFixedSize(true)
//            for (jjal in realm.where(Jjal::class.java).findAll()) {
//                Log.d("JjalJub", "${jjal.id}")
//                Log.d("JjalJub", "${jjal.path}")
//                val fileUri = Uri.parse(jjal.path)
//            }
        }
        if (intent?.action.toString() == "com.sec.android.app.myfiles.PICK_DATA") {
            isUpload = true
        }
    }
}
