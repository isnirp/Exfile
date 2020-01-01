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
import com.flimbis.exfile.view.home.ExFilesFragment
import com.flimbis.exfile.viewmodel.FileViewModel

class ExFileAdapter(val exfileFragment: ExFilesFragment, val context: Context?) : BaseAdapter() {
    private var listener: OnFileClickedListener? = null
    private var items = listOf<FileModel>()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val fileBinding: ItemsFileBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.items_file, parent, false)
        val viewHolder = ViewHolder(fileBinding)

        val fileModel: FileModel = items.get(position)
        viewHolder.bind(fileModel)

        val view = fileBinding.root

        view.setOnClickListener { listener!!.onFileClicked(fileModel) }
        view.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(v: View?): Boolean {
                exfileFragment.actionModeActivated(fileModel)
                v?.isSelected = true
                return true
            }
        })

        fileBinding.pop.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                listener!!.onPopMenuClicked(v, fileModel)
            }
        })

        return view
    }

    override fun getItem(position: Int): Any = items.get(position)

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = items.size

    fun setFileClickedListener(listener: OnFileClickedListener) {
        this.listener = listener
    }

    fun updateDirectory(items: List<FileModel>) {
        this.items = items
        notifyDataSetChanged()
    }

    class ViewHolder(var binding: ItemsFileBinding) {
        fun bind(fileModel: FileModel) {
            binding.fileModel = FileViewModel(fileModel)
        }
    }

    interface OnFileClickedListener {
        fun onFileClicked(fileModel: FileModel)

        fun onPopMenuClicked(v: View, fileModel: FileModel)
    }

}