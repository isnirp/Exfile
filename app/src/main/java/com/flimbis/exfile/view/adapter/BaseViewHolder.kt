package com.flimbis.exfile.view.adapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.flimbis.exfile.BR

open class BaseViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(obj: Any){
        binding.setVariable(BR.obj, obj);
        // forces the bindings to run immediately instead of delaying them until the next frame
        binding.executePendingBindings();
    }
}