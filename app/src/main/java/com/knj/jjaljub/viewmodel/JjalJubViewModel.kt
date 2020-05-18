package com.knj.jjaljub.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.knj.jjaljub.model.Jjal
import com.knj.jjaljub.model.JjalDao

class JjalJubViewModel(private val dao: JjalDao) : ViewModel() {
    val item : LiveData<PagedList<Jjal>> = LivePagedListBuilder(dao.getAll(), 10).build()
    fun saveJjal(jjal: Jjal) = dao.insert(jjal)
}