package com.flimbis.exfile.view.adapter

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.flimbis.exfile.R
import com.flimbis.exfile.ctx
import com.flimbis.exfile.databinding.ItemsFileBinding
import com.flimbis.exfile.model.FileModel
import com.flimbis.exfile.viewmodel.FilesViewModel

class FileAdapter : RecyclerView.Adapter<FileAdapter.FileViewHolder>() {
     var items = listOf<FileModel>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val fileBinding: ItemsFileBinding  = DataBindingUtil.inflate(LayoutInflater.from(parent?.ctx), R.layout.items_file,parent, false)
        return FileViewHolder(fileBinding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        val binding: ItemsFileBinding = holder.binding
        binding.viewModel = FilesViewModel(items[position])
    }

    fun fetchFiles(filesList: List<FileModel>){
        this.items = filesList
        notifyDataSetChanged()
    }

    class FileViewHolder(var binding: ItemsFileBinding) : RecyclerView.ViewHolder(binding.lnrLayout)
}