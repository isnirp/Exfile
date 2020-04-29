package com.flimbis.exfile.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.flimbis.exfile.R
import com.flimbis.exfile.ctx
import com.flimbis.exfile.model.FileModel

class BreadcrumbAdapter() : androidx.recyclerview.widget.RecyclerView.Adapter<BreadcrumbAdapter.FileViewHolder>() {

    private var items = listOf<FileModel>()
    var onItemClickListener: ((FileModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val view: View = LayoutInflater.from(parent.ctx).inflate(R.layout.items_breadcrumb, parent, false)

        return FileViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun updateBreadcrumbList(filesList: List<FileModel>) {
        this.items = filesList
        notifyDataSetChanged()
    }

    inner class FileViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView), View.OnClickListener {
        init {
            itemView.setOnClickListener(this)
        }

        fun bind(fileModel: FileModel) {
            val path = itemView.findViewById<TextView>(R.id.txt_breadcrumb)
            if (fileModel.name == "exHome") path.text = "/"
            else path.text = fileModel.name
        }

        override fun onClick(v: View?) {
            onItemClickListener?.invoke(items[adapterPosition])
        }
    }
}
