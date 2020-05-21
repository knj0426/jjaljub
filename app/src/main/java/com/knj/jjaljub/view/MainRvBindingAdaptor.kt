package com.knj.jjaljub.view

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