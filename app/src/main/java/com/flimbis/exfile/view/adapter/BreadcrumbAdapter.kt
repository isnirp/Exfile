package com.flimbis.exfile.view.adapter

import androidx.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.ViewGroup
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
 
    private var items: List<FileModel> = listOf()
    private var listener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val fileBinding: ItemsFileBinding  = DataBindingUtil.inflate(LayoutInflater.from(parent.ctx), R.layout.items_breadcrumb,parent, false)
        return FileViewHolder(fileBinding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        val fileModel = items[position]

        holder.bind(fileModel)
        holder.binding.root.setOnClickListener{listener!!.onBreadcrumbItemClicked(fileModel)}

    }

    fun updateBreadcrumbList(filesList: List<FileModel>){
        this.items = filesList
        notifyDataSetChanged()
    }

    fun registerBreadcrumbItemClickListener(itemListener: OnItemClickListener?){
        this.listener = itemListener
    }

    //class FileViewHolder(var binding: ItemsFileBinding, val itemClick: (FileModel)-> Unit) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.lnrLayout){
    class FileViewHolder(var binding: ItemsFileBinding) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root){
        fun bind(fileModel: FileModel){
            binding.fileModel = FileViewModel(fileModel)

        }
    }

    interface OnItemClickListener {
        fun onBreadcrumbItemClicked(fileModel: FileModel)
    }
}