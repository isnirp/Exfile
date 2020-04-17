package com.flimbis.exfile.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil.*
import androidx.recyclerview.widget.RecyclerView
import com.flimbis.exfile.R
import com.flimbis.exfile.ctx
import com.flimbis.exfile.databinding.ItemsFileBinding
import com.flimbis.exfile.model.FileModel
import com.flimbis.exfile.viewmodel.FileViewModel
import androidx.recyclerview.selection.ItemDetailsLookup
import android.graphics.BitmapFactory
import com.flimbis.exfile.databinding.ItemsFileGridBinding
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.amulyakhare.textdrawable.TextDrawable
import android.graphics.Color

class ExFileAdapter(private val itemClick: (FileModel) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items = listOf<FileModel>()
    private var listener: OnFileClickedListener? = null
    private var viewType: Int = 0
    private val LIST_VIEW = 0
    private val GRID_VIEW = 1

    private var builder: TextDrawable.IBuilder? = null
    private var generator: ColorGenerator? = null

    init {
        builder = TextDrawable.builder()
                .beginConfig()
                .textColor(Color.WHITE)
                .toUpperCase()
                .endConfig()
                .round();

        generator = ColorGenerator.MATERIAL;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            GRID_VIEW -> {
                val gridFileBinding: ItemsFileGridBinding = inflate(LayoutInflater.from(parent?.ctx), R.layout.items_file_grid, parent, false)
                RexGridViewHolder(gridFileBinding, itemClick)
            }
            else -> {
                val fileBinding: ItemsFileBinding = inflate(LayoutInflater.from(parent?.ctx), R.layout.items_file, parent, false)
                RexListViewHolder(fileBinding, itemClick)
            }
        }

    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int {
        return when (viewType) {
            1 -> {
                GRID_VIEW
            }
            else -> {
                LIST_VIEW
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (viewType == LIST_VIEW) {
            val lstHolder = holder as RexListViewHolder
            lstHolder.bindView(items[position])
        } else {
            val lstGridHolder = holder as RexGridViewHolder
            lstGridHolder.bindView(items[position])
        }
    }

    fun updateDirectory(items: List<FileModel>) {
        this.items = items
        notifyDataSetChanged()
    }

    fun setViewType(i: Int) {
        this.viewType = i
    }

    fun renderImages(img: String, ext: String, v: ImageView) {
        val mimeFilter = listOf(
                "jpeg",
                "jpg",
                "png",
                "gif"
        )

        if (ext in mimeFilter) {
            v.setImageBitmap(BitmapFactory.decodeFile(img))
        }
    }

    inner class RexListViewHolder(private val binding: ItemsFileBinding, private val itemClick: (FileModel) -> Unit) : RecyclerView.ViewHolder(binding.root) {

        fun bindView(fileModel: FileModel, isActivated: Boolean = false) {
            binding.fileModel = FileViewModel(fileModel)
            itemView.isActivated = isActivated

            binding.root.setOnClickListener { itemClick(fileModel) }
            binding.pop.setOnClickListener { v -> listener!!.onPopMenuClicked(v, fileModel) }

            renderImages(fileModel.path, fileModel.ext!!, binding.imgFile)
        }

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> =
                object : ItemDetailsLookup.ItemDetails<Long>() {
                    override fun getPosition(): Int = adapterPosition
                    override fun getSelectionKey(): Long? = itemId
                }
    }

    inner class RexGridViewHolder(private val binding: ItemsFileGridBinding, private val itemClick: (FileModel) -> Unit) : RecyclerView.ViewHolder(binding.root) {

        fun bindView(fileModel: FileModel, isActivated: Boolean = false) {
            binding.fileModel = FileViewModel(fileModel)
            itemView.isActivated = isActivated

            binding.root.setOnClickListener { itemClick(fileModel) }

            renderImages(fileModel.path, fileModel.ext!!, binding.imgFile)

            val mColor = generator!!.randomColor
            val mDrawable = builder!!.build(fileModel.name.substring(0, 1), mColor)

            binding.imgCover.setImageDrawable(mDrawable)
        }

    }

    fun setFileClickedListener(listener: OnFileClickedListener?) {
        this.listener = listener
    }

    interface OnFileClickedListener {
        fun onPopMenuClicked(v: View, fileModel: FileModel)
    }
}
