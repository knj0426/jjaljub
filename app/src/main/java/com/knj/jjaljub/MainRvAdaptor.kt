package com.knj.jjaljub

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.util.SparseArray
import android.view.*
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.selection.ItemDetailsLookup
import io.realm.Realm
import io.realm.kotlin.where

class MainRvAdaptor(val context: Context, val jjalList: ArrayList<Jjal>) :
        RecyclerView.Adapter<MainRvAdaptor.Holder>() {
    var checkedList :ArrayList<Jjal> = ArrayList()

    var actionMode = false
    val adaptor :MainRvAdaptor = this
    init {
        setHasStableIds(true)
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnLongClickListener, View.OnClickListener {
        init {
            itemView.setOnLongClickListener(this)
            itemView.setOnClickListener(this)
        }
        val jjalImage = itemView.findViewById<ImageView>(R.id.jjalImage)

        fun bind (jjal:Jjal, context: Context) {
            val uri = Uri.parse(jjal.path)
            jjalImage.setImageURI(uri)
        }

        inner class ActionModeCallback : ActionMode.Callback {
            override fun onActionItemClicked(p0: ActionMode?, p1: MenuItem?): Boolean {
                for (jjal in checkedList) {
                    jjalList.remove(jjal)
                    val defaultRealm = Realm.getDefaultInstance()
                    defaultRealm.executeTransaction {realm ->
                        val deleteItem = realm.where<Jjal>().equalTo("id", jjal.id).findFirst()!!
                        deleteItem.deleteFromRealm()
                    }
                }
                p0?.finish()
                return true
            }

            override fun onCreateActionMode(p0: ActionMode?, p1: Menu?): Boolean {
                actionMode = true
                adaptor.notifyDataSetChanged()
                p1?.add("Delete")
                return true
            }

            override fun onPrepareActionMode(p0: ActionMode?, p1: Menu?): Boolean {
                return true
            }

            override fun onDestroyActionMode(p0: ActionMode?) {
                actionMode = false
                adaptor.notifyDataSetChanged()
            }

        }

        override fun onLongClick(v: View?): Boolean {
            v?.findViewById<CheckBox>(R.id.checkBox)?.isChecked = true

            (v?.context as Activity).startActionMode(ActionModeCallback())
            return true
        }


        override fun onClick(v: View?) {
            if (actionMode) {
                Log.d("nojung", "it is clicked")
                v?.findViewById<CheckBox>(R.id.checkBox)?.toggle()
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return jjalList[position].id.toLong()
    }

    override fun getItemCount(): Int {
        return jjalList.size
    }

    override fun onBindViewHolder(p0: Holder, p1: Int) {
        p0.bind(jjalList[p1], context)
        p0.itemView.findViewById<CheckBox>(R.id.checkBox).setOnCheckedChangeListener{
            buttonView, isChecked ->
            if (isChecked) {
                checkedList.add(jjalList[p1])
            } else {
                checkedList.remove(jjalList[p1])
            }
        }
        if (actionMode) {
            p0.itemView.findViewById<CheckBox>(R.id.checkBox).visibility = View.VISIBLE
        } else {
            p0.itemView.findViewById<CheckBox>(R.id.checkBox).isChecked = false
            p0.itemView.findViewById<CheckBox>(R.id.checkBox).visibility = View.GONE
        }
        if ((context as MainActivity).isUpload) {
            p0.itemView.setOnClickListener(View.OnClickListener { v ->
                context.handleUpload(Uri.parse(jjalList[p1].path))
            })
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.main_rv_item, p0, false)
        view.clipToOutline = true
        return Holder(view)
    }
}