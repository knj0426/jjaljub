package com.knj.jjaljub.viewmodel

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.knj.jjaljub.model.Jjal
import com.knj.jjaljub.model.JjalDao

class JjalJubViewModel(dao: JjalDao) : ViewModel() {
    var isUpload = false
    val item: LiveData<PagedList<Jjal>> = LivePagedListBuilder(dao.getAll(), 10).build()
    lateinit var context: Context

    fun onClick(uri: String) {
        if (isUpload) {
            // Parse file path as URI
            val result = Intent()
            result.data = Uri.parse(uri)
            (context as AppCompatActivity).setResult(Activity.RESULT_OK, result)
            Log.d("JjalJub", "path = $uri")
            (context as AppCompatActivity).finish()
        }
    }
}