package com.flimbis.exfile.view.adapter

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.flimbis.exfile.R
import com.flimbis.exfile.databinding.ItemsFileBinding
import com.flimbis.exfile.model.FileModel
import com.flimbis.exfile.viewmodel.FilesViewModel

class FileAdapter(val items: List<FileModel>) : RecyclerView.Adapter<FileAdapter.FileViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val fileBinding: ItemsFileBinding  = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.items_file,parent, false)
        //fileBinding.viewmodel =
        return FileViewHolder(fileBinding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        val binding = holder.binding
        binding.viewmodel = FilesViewModel(items[position])
    }

    class FileViewHolder(val binding: ItemsFileBinding) : RecyclerView.ViewHolder(binding.txtFile)
}