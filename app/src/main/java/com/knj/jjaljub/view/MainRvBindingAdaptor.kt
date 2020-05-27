package com.knj.jjaljub.view

import android.view.KeyEvent
import androidx.appcompat.widget.AppCompatEditText
import androidx.databinding.BindingAdapter
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import com.knj.jjaljub.model.Jjal
import com.knj.jjaljub.viewmodel.JjalJubViewModel


@BindingAdapter("jjalList", "viewModel")
fun setJjalList(view: RecyclerView, items: PagedList<Jjal>?, vm: JjalJubViewModel) {
    view.adapter?.run {
        if (this is JjalAdapter) this.submitList(items)
    } ?: run {
        JjalAdapter(vm).run {
            view.adapter = this
            this.submitList(items)
        }
    }
}

interface OnEditorActionListener {
    fun OnEditorAction(actionId: Int, event: KeyEvent?)
}
@BindingAdapter("onEditorAction")
fun setOnEditorAction(view: AppCompatEditText, listener: OnEditorActionListener) {
    if (listener == null) {
        view.setOnEditorActionListener(null)
    } else {
        view.setOnEditorActionListener { v, actionId, event ->
            listener.OnEditorAction(actionId, event)
            true
        }
    }
}