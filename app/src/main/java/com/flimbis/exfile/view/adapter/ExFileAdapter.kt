package com.flimbis.exfile.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.databinding.DataBindingUtil
import com.flimbis.exfile.R
import com.flimbis.exfile.databinding.ItemsFileBinding
import com.flimbis.exfile.model.FileModel
import com.flimbis.exfile.viewmodel.FileViewModel

class ExFileAdapter(val context: Context?, val items: List<FileModel>): BaseAdapter(){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val fileBinding: ItemsFileBinding  = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.items_file,parent, false)
        val viewHolder = ViewHolder(fileBinding)

        val fileModel: FileModel = items.get(position)
        viewHolder.bind(fileModel)

        val view = fileBinding.root

        return view
    }

    override fun getItem(position: Int): Any = items.get(position)

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = items.size

    class ViewHolder(var binding: ItemsFileBinding){
        fun bind(fileModel: FileModel){
            binding.fileModel = FileViewModel(fileModel)
        }
    }

}