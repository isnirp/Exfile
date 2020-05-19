package com.flimbis.exfile.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.flimbis.exfile.R
import com.flimbis.exfile.databinding.ItemsHomeBinding
import com.flimbis.exfile.model.FileModel
import com.flimbis.exfile.util.ctx
import kotlinx.android.synthetic.main.list_recent.view.*

class HomeAdapter(private val itemClick: (String) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items = listOf<FileModel>()
    private val menu = 0
    private val recent = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            recent -> {
                val recentView = LayoutInflater.from(parent.ctx).inflate(R.layout.list_recent, parent, false)
                RecentViewHolder(recentView, items)
            }
            else -> {
                val homeBinding = DataBindingUtil.inflate<ItemsHomeBinding>(LayoutInflater.from(parent?.ctx), R.layout.items_home, parent, false)
                return MenuViewHolder(homeBinding, itemClick)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int {
        return when (0) {
            1 -> recent
            else -> menu
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            recent -> {
                val recentHolder = holder as RecentViewHolder
                recentHolder.bindView(items[position])
            }
            else -> {
                val menuHolder = holder as MenuViewHolder
                menuHolder.bindView(items[position])
            }
        }

    }

    fun updateHomeItems(items: List<FileModel>) {
        this.items = items
    }

    class MenuViewHolder(private val binding: ItemsHomeBinding, private val itemClick: (String) -> Unit) : BaseViewHolder(binding) {
        fun bindView(fileModel: FileModel) {
            this.bind(fileModel)
            binding.root.setOnClickListener { itemClick(fileModel.path) }
        }
    }

    class RecentViewHolder(itemView: View, private val recentItems: List<FileModel>) : RecyclerView.ViewHolder(itemView) {
        fun bindView(fileModel: FileModel) {
            itemView.lst_recent.layoutManager = LinearLayoutManager(itemView.ctx, LinearLayoutManager.HORIZONTAL, false)
            itemView.lst_recent.adapter = RecentAdapter(recentItems)
        }
    }
}