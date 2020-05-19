package com.flimbis.exfile.view.adapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.flimbis.exfile.BR

class BaseViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(obj: Any){
        binding.setVariable(BR.obj, obj);
        binding.executePendingBindings();
    }
}