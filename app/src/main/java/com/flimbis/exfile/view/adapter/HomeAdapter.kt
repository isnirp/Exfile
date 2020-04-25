package com.flimbis.exfile.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.flimbis.exfile.R
import com.flimbis.exfile.ctx
import com.flimbis.exfile.databinding.ItemsHomeBinding
import com.flimbis.exfile.model.FileModel
import com.flimbis.exfile.viewmodel.FileViewModel

class HomeAdapter(private val itemClick: (String) -> Unit) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    private var items = listOf<FileModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val homeBinding = DataBindingUtil.inflate<ItemsHomeBinding>(LayoutInflater.from(parent?.ctx), R.layout.items_home, parent, false)
        return ViewHolder(homeBinding, itemClick)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(items.get(position))
    }

    fun updateHomeItems(items: List<FileModel>) {
        this.items = items
    }

    class ViewHolder(private val binding: ItemsHomeBinding, private val itemClick: (String) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(fileModel: FileModel) {
            binding.fileModel = FileViewModel(fileModel)
            binding.root.setOnClickListener { itemClick(fileModel.path) }
        }
    }
}