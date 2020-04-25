package com.flimbis.exfile.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.flimbis.exfile.R
import com.flimbis.exfile.ctx
import com.flimbis.exfile.databinding.ItemsFileRecentBinding
import com.flimbis.exfile.model.FileModel
import com.flimbis.exfile.viewmodel.FileViewModel

class RecentAdapter(private val items: List<FileModel>) : RecyclerView.Adapter<RecentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemsFileRecentBinding = DataBindingUtil.inflate<ItemsFileRecentBinding>(LayoutInflater.from(parent.ctx), R.layout.items_file_recent, parent, false)
        return ViewHolder(itemsFileRecentBinding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindViews(items[position])
    }

    class ViewHolder(private val binding: ItemsFileRecentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindViews(fileModel: FileModel) {
            binding.fileModel = FileViewModel(fileModel)
        }
    }
}