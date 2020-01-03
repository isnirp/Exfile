package com.flimbis.exfile.viewmodel

import androidx.databinding.BaseObservable
import com.flimbis.exfile.model.FileModel
import com.flimbis.exfile.model.FileType
import com.flimbis.exfile.util.convertFileSizeToMB
import java.text.SimpleDateFormat
import java.util.*

class FileViewModel(val fileModel: FileModel) : BaseObservable() {
    fun getName(): String = fileModel.name

    fun getType(): String = if (fileModel.isDirectory) FileType.DIRECTORY.type else FileType.FILE.type

    fun getSize(): String = String.format("%.2f", convertFileSizeToMB(fileModel.size))+"MB"

    fun getExt(): String = fileModel.ext

    fun getPath(): String = fileModel.path

    fun getLastModified(): String = formatLastModified(fileModel.lastModified)
    //fun getLastModified(): String = fileModel.lastModified

    //check users location and fix date format
    fun formatLastModified(lastModified: Long): String {
        //val formatter = SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS")
        val formatter = SimpleDateFormat("dd/MM/yyyy")

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = lastModified

        return formatter.format(calendar.time)
    }
}