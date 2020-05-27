package com.knj.jjaljub.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.knj.jjaljub.R
import com.knj.jjaljub.databinding.ItemJjalBinding
import com.knj.jjaljub.model.Jjal
import com.knj.jjaljub.viewmodel.JjalJubViewModel
import kotlinx.android.synthetic.main.item_jjal.view.*

class JjalAdapter(private val vm: JjalJubViewModel) :
    PagedListAdapter<Jjal, JjalAdapter.JjalHolder>(DIFF_CALLBACK) {
    init {
        vm.adapter = this
    }


    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Jjal>() {
            override fun areItemsTheSame(oldItem: Jjal, newItem: Jjal) = oldItem.id == newItem.id

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: Jjal, newItem: Jjal) = oldItem == newItem

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JjalHolder {
        // Create ViewHolder and return
        return JjalHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_jjal, parent, false)
        )
    }

    override fun onBindViewHolder(holder: JjalHolder, position: Int) {
        getItem(position)?.run {
            // bind Jjal and ViewModel with item layout
            holder.binding.item = this
            holder.binding.vm = vm
            holder.itemView.checkBox.visibility = if (vm.isActionMode) View.VISIBLE else View.GONE
        }
    }

    class JjalHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            view.setOnClickListener {
                binding.vm?.onClick(binding.item!!.path)
            }
            view.setOnLongClickListener {
                binding.vm?.onLongClick()
                true
            }
        }

        val binding: ItemJjalBinding = DataBindingUtil.bind(view)!!
    }
}