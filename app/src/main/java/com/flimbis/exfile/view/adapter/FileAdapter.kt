package com.flimbis.exfile.view.adapter

import androidx.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.flimbis.exfile.R
import com.flimbis.exfile.ctx
import com.flimbis.exfile.databinding.ItemsFileBinding
import com.flimbis.exfile.model.FileModel
import com.flimbis.exfile.viewmodel.FileViewModel

//class FileAdapter(val items: List<FileModel>, val itemClick: (FileModel)-> Unit) : androidx.recyclerview.widget.RecyclerView.Adapter<FileAdapter.FileViewHolder>() {
class FileAdapter(val items: List<FileModel>) : androidx.recyclerview.widget.RecyclerView.Adapter<FileAdapter.FileViewHolder>() {
 
    private var listener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val fileBinding: ItemsFileBinding  = DataBindingUtil.inflate(LayoutInflater.from(parent.ctx), R.layout.items_file,parent, false)
        return FileViewHolder(fileBinding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        val fileModel = items[position]

        //val binding: ItemsFileBinding = holder.binding
        //binding.fileModel = FileViewModel(fileModel)
        holder.bind(fileModel)
        holder.binding.root.setOnClickListener{listener!!.onItemClicked(fileModel)}

    }

    /*fun fetchFiles(filesList: List<FileModel>){
        this.items = filesList
        notifyDataSetChanged()
    }*/

    fun registerItemClickListener(itemListener: OnItemClickListener?){
        listener = itemListener
    }

    //class FileViewHolder(var binding: ItemsFileBinding, val itemClick: (FileModel)-> Unit) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.lnrLayout){
    class FileViewHolder(var binding: ItemsFileBinding) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root){
        fun bind(fileModel: FileModel){
            binding.fileModel = FileViewModel(fileModel)
            //binding.root.setOnClickListener{itemClick(fileModel)}

        }
    }

    interface OnItemClickListener {
        fun onItemClicked(fileModel: FileModel)
    }
}