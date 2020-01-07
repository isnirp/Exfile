package com.flimbis.exfile.viewmodel

import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BaseObservable
import androidx.databinding.BindingAdapter
import com.flimbis.exfile.R
import com.flimbis.exfile.model.FileModel
import com.flimbis.exfile.model.FileType
import com.flimbis.exfile.util.convertFileSizeToMB
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class FileViewModel(val fileModel: FileModel) : BaseObservable() {
    fun getName(): String = fileModel.name

    fun getType(): String = if (fileModel.isDirectory) FileType.DIRECTORY.type else FileType.FILE.type

    fun getSize(): String = String.format("%.2f", convertFileSizeToMB(fileModel.size)) + "MB"

    fun getExt(): String = fileModel.ext

    fun getPath(): String = fileModel.path

    fun getLastModified(): String = formatLastModified(fileModel.lastModified)


    //check users location and fix date format
    fun formatLastModified(lastModified: Long): String {
        //val formatter = SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS")
        val formatter = SimpleDateFormat("dd/MM/yyyy")

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = lastModified

        return formatter.format(calendar.time)
    }

    companion object {
        @BindingAdapter("app:imageSrc")
        @JvmStatic
        fun setImageResource(v: ImageView, imageSrc: Int) {

            /*val mimeFilter = listOf(
                    "jpeg",
                    "jpg",
                    "png",
                    "gif"
            )
            if (imgUrl in mimeFilter) {
                Picasso.get().load(Uri.parse(imgUrl)).placeholder(R.drawable.ic_action_folder).into(v)
            } else v.setImageResource(R.drawable.ic_action_folder)*/
            v.setImageResource(R.drawable.ic_action_folder)
        }

        @BindingAdapter("app:imageSrc")
        @JvmStatic
        fun setImageResource(v: ImageView, imageSrc: String) {

            /*val mimeFilter = listOf(
                    "jpeg",
                    "jpg",
                    "png",
                    "gif"
            )
            if (imgUrl in mimeFilter) {
                Picasso.get().load(Uri.parse(imgUrl)).placeholder(R.drawable.ic_action_folder).into(v)
            } else v.setImageResource(R.drawable.ic_action_folder)*/
            v.setImageResource(R.drawable.ic_action_folder)
        }
    }
}