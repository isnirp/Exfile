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
import com.flimbis.exfile.databinding.ItemsFileGridBinding
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.amulyakhare.textdrawable.TextDrawable
import android.graphics.Color




class RexFileAdapter(private val itemClick:(FileModel)->Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items = listOf<FileModel>()
    private var listener: OnFileClickedListener? = null
    private var viewType: Int = 0
    private val LIST_VIEW = 0
    private val GRID_VIEW = 1

    private var builder: TextDrawable.IBuilder? = null
    private var generator: ColorGenerator? = null

    var tracker: SelectionTracker<Long>? = null

    init {
        //setHasStableIds(true)

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
            LIST_VIEW -> {
                val fileBinding: ItemsFileBinding = inflate(LayoutInflater.from(parent?.ctx), R.layout.items_file, parent, false)
                RexViewHolder(fileBinding, itemClick)
            }
            else -> {
                val gridFileBinding: ItemsFileGridBinding = inflate(LayoutInflater.from(parent?.ctx), R.layout.items_file_grid, parent, false)
                RexGridViewHolder(gridFileBinding, itemClick)
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
            val lstHolder = holder as RexViewHolder
            lstHolder.bindView(items[position])
        } else {
            val lstGridHolder = holder as RexGridViewHolder
            lstGridHolder.bindView(items[position])
        }

        /*tracker?.let {
            lstHolder.bindView(items[position], it.isSelected(position.toLong()))
        }*/
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
            //Picasso.get().load(Uri.parse(img)).placeholder(R.drawable.ic_action_folder).into(v)
            v.setImageBitmap(BitmapFactory.decodeFile(img))

        }
    }

    inner class RexViewHolder(private val binding: ItemsFileBinding, private val itemClick:(FileModel)->Unit) : RecyclerView.ViewHolder(binding.root) {

        fun bindView(fileModel: FileModel, isActivated: Boolean = false) {
            binding.fileModel = FileViewModel(fileModel)
            itemView.isActivated = isActivated

//            binding.root.setOnClickListener { listener!!.onFileClicked(fileModel) }
            binding.root.setOnClickListener { itemClick(fileModel) }
            binding.pop.setOnClickListener { v -> listener!!.onPopMenuClicked(v, fileModel) }

            renderImages(fileModel.path, fileModel.ext, binding.imgFile)

        }

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> =
                object : ItemDetailsLookup.ItemDetails<Long>() {
                    override fun getPosition(): Int = adapterPosition
                    override fun getSelectionKey(): Long? = itemId
                }
    }

    inner class RexGridViewHolder(private val binding: ItemsFileGridBinding, private val itemClick:(FileModel)->Unit) : RecyclerView.ViewHolder(binding.root) {

        fun bindView(fileModel: FileModel, isActivated: Boolean = false) {
            binding.fileModel = FileViewModel(fileModel)
            itemView.isActivated = isActivated

//            binding.root.setOnClickListener { listener!!.onFileClicked(fileModel) }
            binding.root.setOnClickListener { itemClick(fileModel) }

            renderImages(fileModel.path, fileModel.ext, binding.imgFile)

            val mColor = generator!!.getRandomColor()
            val mDrawable = builder!!.build(fileModel.name.substring(0, 1), mColor)

            binding.imgCover.setImageDrawable(mDrawable)
        }

    }

    fun setFileClickedListener(listener: OnFileClickedListener?) {
        this.listener = listener
    }

    interface OnFileClickedListener {
        //fun onFileClicked(fileModel: FileModel)

        fun onPopMenuClicked(v: View, fileModel: FileModel)
    }
}