package com.flimbis.exfile.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.flimbis.exfile.R
import com.flimbis.exfile.model.FileModel
import com.flimbis.exfile.util.ctx
import kotlinx.android.synthetic.main.items_delete.view.*

class DeleteFilesAdapter(val items: List<FileModel>) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val layoutInflater = LayoutInflater.from(parent?.ctx)
        val view: View = layoutInflater.inflate(R.layout.items_delete, parent, false)

        val vh = ViewHolder(view)
        vh.bind(items[position])

        return view
    }

    override fun getItem(position: Int): Any = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = items.size

    class ViewHolder(val itemView: View) {
        fun bind(fileModel: FileModel) {
            val fileToDelete = itemView.findViewById<TextView>(R.id.txt_delete_file)
            fileToDelete.text = fileModel.name
        }
    }
}