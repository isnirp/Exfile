package com.flimbis.exfile.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil.*
import androidx.recyclerview.widget.RecyclerView
import com.flimbis.exfile.R
import com.flimbis.exfile.databinding.ItemsFileBinding
import com.flimbis.exfile.model.FileModel
import androidx.recyclerview.selection.ItemDetailsLookup
import android.graphics.BitmapFactory
import com.flimbis.exfile.databinding.ItemsFileGridBinding
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.amulyakhare.textdrawable.TextDrawable
import android.graphics.Color
import com.flimbis.exfile.MainActivity
import com.flimbis.exfile.util.ctx
import com.flimbis.exfile.view.ExFilesFragment

class ExFileAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items = listOf<FileModel>()
    private var listener: OnFileClickedListener? = null
    private var viewType: Int = 0
    private val LIST_VIEW = 0
    private val GRID_VIEW = 1

    private var builder: TextDrawable.IBuilder? = null
    private var generator: ColorGenerator? = null

    private var selectedItems = mutableListOf<FileModel>() //for contextual bar

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
                val gridFileBinding: ItemsFileGridBinding = inflate(LayoutInflater.from(parent.ctx), R.layout.items_file_grid, parent, false)
                RexGridViewHolder(gridFileBinding)
            }
            else -> {
                val fileBinding: ItemsFileBinding = inflate(LayoutInflater.from(parent.ctx), R.layout.items_file, parent, false)
                RexListViewHolder(fileBinding)
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

    fun renderImages(img: String, v: ImageView) {
        //display images in CircleImageView
        v.visibility = View.VISIBLE
        v.setImageBitmap(BitmapFactory.decodeFile(img))
    }

    fun trackSelectedItems(fileModel: FileModel, view: View) {
        if (!selectedItems.contains(fileModel)) {
            //view.setBackgroundColor(Color.LTGRAY)
            view.setBackgroundResource(R.color.selection)
            listener!!.selectedItemsInActionMode(fileModel, selectedItems.size + 1)
            selectedItems.add(fileModel)
            //(activity as AppCompatActivity).supportActionBar!!.title = "items "+ (selectedItems.size + 1)
        } else {
            view.setBackgroundColor(Color.TRANSPARENT)
            listener!!.selectedItemsInActionMode(fileModel, selectedItems.size - 1)
            selectedItems.remove(fileModel)
        }

    }

    fun clearSelection() {
        selectedItems = mutableListOf<FileModel>()
        notifyDataSetChanged()
    }

    inner class RexListViewHolder(private val binding: ItemsFileBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindView(fileModel: FileModel) {
            binding.fileModel = fileModel

            binding.root.setOnClickListener {
                if (MainActivity.isInActionMode) {
                    //add to selected items
                    trackSelectedItems(fileModel, it)
                } else
                    listener!!.onFileClicked(fileModel)
            }

            binding.pop.setOnClickListener { v -> listener!!.onPopMenuClicked(v, fileModel) }

            binding.root.setOnLongClickListener {
                selectedItems.add(fileModel)
                it.setBackgroundResource(R.color.selection)
                listener!!.onFileLongClicked(fileModel)
                true
            }

        }

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> =
                object : ItemDetailsLookup.ItemDetails<Long>() {
                    override fun getPosition(): Int = adapterPosition
                    override fun getSelectionKey(): Long? = itemId
                }
    }

    inner class RexGridViewHolder(private val binding: ItemsFileGridBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindView(fileModel: FileModel, isActivated: Boolean = false) {
            binding.fileModel = fileModel
            itemView.isActivated = isActivated

            binding.root.setOnClickListener { listener!!.onFileClicked(fileModel) }

            if (fileModel.type != "folder") {
                val mimeFilter = listOf(
                        "jpeg",
                        "jpg",
                        "png",
                        "gif"
                )

                if (fileModel.ext in mimeFilter) {
                    //display images in CircleImageView
                    renderImages(fileModel.path, binding.imgCover)
                    binding.imgFile.setImageResource(R.drawable.ic_drive_file)
                } else {
                    val mColor = generator!!.randomColor
                    val mDrawable = builder!!.build(fileModel.name.substring(0, 1), mColor)

                    binding.imgCover.setImageDrawable(mDrawable)
                    binding.imgFile.setImageResource(R.drawable.ic_drive_file)
                }
            } else {
                val mColor = generator!!.randomColor
                val mDrawable = builder!!.build(fileModel.name.substring(0, 1), mColor)

                binding.imgCover.setImageDrawable(mDrawable)
            }
        }

    }

    fun setFileClickedListener(listener: OnFileClickedListener?) {
        this.listener = listener
    }

    interface OnFileClickedListener {
        fun onFileClicked(fileModel: FileModel)

        fun onFileLongClicked(fileModel: FileModel)

        fun onPopMenuClicked(v: View, fileModel: FileModel)

        fun selectedItemsInActionMode(fileModel: FileModel, size: Int)

        //fun removeSelection()
    }
}
