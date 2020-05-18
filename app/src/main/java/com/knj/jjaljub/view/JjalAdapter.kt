package com.knj.jjaljub.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.knj.jjaljub.R
import com.knj.jjaljub.databinding.ItemJjalBinding
import com.knj.jjaljub.model.Jjal
import com.knj.jjaljub.viewmodel.JjalJubViewModel

class JjalAdapter(private val vm : JjalJubViewModel) : PagedListAdapter<Jjal, JjalAdapter.JjalHolder>(DIFF_CALLBACK) {
    class JjalHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: ItemJjalBinding = DataBindingUtil.bind(view)!!
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Jjal>() {
            override fun areItemsTheSame(oldItem: Jjal, newItem: Jjal) = oldItem.id == newItem.id

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: Jjal, newItem: Jjal) = oldItem == newItem

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JjalHolder {
        return JjalHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_jjal, parent, false))
    }

    override fun onBindViewHolder(holder: JjalHolder, position: Int) {
        getItem(position)?.run {
            holder.binding.item = this
            holder.binding.vm = vm
        }
    }
}