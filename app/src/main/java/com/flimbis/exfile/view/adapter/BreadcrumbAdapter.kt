package com.flimbis.exfile.view.adapter

import androidx.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.flimbis.exfile.R
import com.flimbis.exfile.ctx
import com.flimbis.exfile.databinding.ItemsFileBinding
import com.flimbis.exfile.model.FileModel
import com.flimbis.exfile.viewmodel.FileViewModel


/*
* DEPRECATED
* */
//class BreadcrumbAdapter(val items: List<FileModel>, val itemClick: (FileModel)-> Unit) : androidx.recyclerview.widget.RecyclerView.Adapter<BreadcrumbAdapter.FileViewHolder>() {
class BreadcrumbAdapter() : androidx.recyclerview.widget.RecyclerView.Adapter<BreadcrumbAdapter.FileViewHolder>() {

    private var items = listOf<FileModel>()
    var onItemClickListener: ((FileModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val layoutInflater = LayoutInflater.from(parent.ctx)
        val view: View = layoutInflater.inflate(R.layout.items_breadcrumb, parent, false)

        return FileViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        val fileModel = items[position]

        holder.bind(fileModel)
        //holder.binding.root.setOnClickListener{listener!!.onBreadcrumbItemClicked(fileModel)}

    }

    fun updateBreadcrumbList(filesList: List<FileModel>) {
        this.items = filesList
        notifyDataSetChanged()
    }

    //class FileViewHolder(var binding: ItemsFileBinding, val itemClick: (FileModel)-> Unit) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.lnrLayout){
    inner class FileViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView), View.OnClickListener {
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