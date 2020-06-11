package com.knj.jjaljub.viewmodel

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import com.knj.jjaljub.model.Jjal
import com.knj.jjaljub.model.JjalDao
import com.knj.jjaljub.view.JjalAdapter
import java.io.File

class JjalJubViewModel(private val dao: JjalDao) : ViewModel() {
    var isUpload = false
//    val item: LiveData<PagedList<Jjal>> = LivePagedListBuilder(dao.getAll(), 10).build()

    val keyword = MutableLiveData<String>()
    val item = Transformations.switchMap(keyword) {
        when(it.isNullOrEmpty()) {
            true -> LivePagedListBuilder(dao.getAll(), 10).build()
            false -> LivePagedListBuilder(dao.get("%$it%"), 10).build()
        }
    }
    init {
        keyword.value = ""
    }
    lateinit var context: Context
    lateinit var adapter: PagedListAdapter<Jjal, JjalAdapter.JjalHolder>
    var isActionMode = false
    var checkedList = ArrayList<Jjal>()

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

    fun onLongClick() {
        (context as AppCompatActivity).startSupportActionMode(ActionModeCallback())
    }

    fun onCheckedChanged(jjal: Jjal, isChecked: Boolean) {
        Log.d("JjalJub", "onCheckedChanged")
        if (isChecked) {
            checkedList.add(jjal)
        } else {
            checkedList.remove(jjal)
        }
    }

    fun onEditorAction(actionId: Int, event: KeyEvent?) {
        when (actionId) {
            EditorInfo.IME_ACTION_SEARCH -> {
                Log.d("JjalJub", "keyword : ${keyword.value}")
            }
        }
    }

    inner class ActionModeCallback : ActionMode.Callback {
        override fun onActionItemClicked(p0: ActionMode?, p1: MenuItem?): Boolean {
            val r = Runnable {
                for (jjal in checkedList) {
                    dao.delete(jjal)
                    context.contentResolver.delete(Uri.parse(jjal.path!!), null, null)
                }
            }
            val t = Thread(r)
            t.start()
            p0?.finish()
            return true
        }

        override fun onCreateActionMode(p0: ActionMode?, p1: Menu?): Boolean {
            isActionMode = true
            adapter.notifyDataSetChanged()
            p1?.add("Delete")
            return true
        }

        override fun onPrepareActionMode(p0: ActionMode?, p1: Menu?): Boolean {
            return true
        }

        override fun onDestroyActionMode(p0: ActionMode?) {
            isActionMode = false
            adapter.notifyDataSetChanged()
        }
    }
}