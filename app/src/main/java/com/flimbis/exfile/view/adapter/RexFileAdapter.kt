package com.flimbis.exfile.view.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.*
import androidx.recyclerview.widget.RecyclerView
import com.flimbis.exfile.R
import com.flimbis.exfile.ctx
import com.flimbis.exfile.databinding.ItemsFileBinding
import com.flimbis.exfile.model.FileModel
import com.flimbis.exfile.viewmodel.FileViewModel
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import com.squareup.picasso.Picasso
import android.graphics.BitmapFactory
import android.graphics.Bitmap



class RexFileAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items = listOf<FileModel>()
    private var listener: OnFileClickedListener? = null

    var tracker: SelectionTracker<Long>? = null

    /*init {
        setHasStableIds(true)
    }*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val fileBinding: ItemsFileBinding = inflate(LayoutInflater.from(parent?.ctx), R.layout.items_file, parent, false)

        return RexViewHolder(fileBinding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val lstHolder = holder as RexViewHolder
        lstHolder.bindView(items[position])
        /*tracker?.let {
            lstHolder.bindView(items[position], it.isSelected(position.toLong()))
        }*/
    }

    fun updateDirectory(items: List<FileModel>) {
        this.items = items
        notifyDataSetChanged()
    }

    inner class RexViewHolder(private val binding: ItemsFileBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindView(fileModel: FileModel, isActivated: Boolean = false) {
            binding.fileModel = FileViewModel(fileModel)
            itemView.isActivated = isActivated

            binding.root.setOnClickListener { listener!!.onFileClicked(fileModel) }
            binding.pop.setOnClickListener { v -> listener!!.onPopMenuClicked(v, fileModel) }

            renderImages(fileModel.path, fileModel.ext, binding.imgFile)

        }

        private fun renderImages(img: String, ext: String, v: ImageView){
            val mimeFilter = listOf(
                    "jpeg",
                    "jpg",
                    "png",
                    "gif"
            )

            if (ext in mimeFilter) {
                //Picasso.get().load(Uri.parse(img)).placeholder(R.drawable.ic_action_folder).into(v)
                v.setImageBitmap(BitmapFactory.decodeFile(img))

            }
        }

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> =
                object : ItemDetailsLookup.ItemDetails<Long>() {
                    override fun getPosition(): Int = adapterPosition
                    override fun getSelectionKey(): Long? = itemId
                }
    }

    fun setFileClickedListener(listener: OnFileClickedListener?) {
        this.listener = listener
    }

    interface OnFileClickedListener {
        fun onFileClicked(fileModel: FileModel)

        fun onPopMenuClicked(v: View, fileModel: FileModel)
    }
}