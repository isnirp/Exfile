package com.flimbis.exfile.viewmodel

import androidx.databinding.BaseObservable
import com.flimbis.exfile.model.FileModel
import com.flimbis.exfile.model.FileType

class FileViewModel(val fileModel: FileModel): BaseObservable() {
    fun getName(): String = fileModel.name

    fun getType(): String = if(fileModel.isDirectory) FileType.DIRECTORY.type else FileType.FILE.type

    fun getSize(): Long = fileModel.size

    fun getExt(): String = fileModel.ext

    fun getPath(): String = fileModel.path

    //fun getDateModified(): String = fileModel.lastModified
}