package com.flimbis.exfile.viewmodel

import androidx.databinding.BaseObservable
import com.flimbis.exfile.model.FileModel

class FileViewModel(val fileModel: FileModel): BaseObservable() {
    fun getName(): String = fileModel.name

    fun getType(): String = fileModel.type

    fun getSize(): Double = fileModel.size
}