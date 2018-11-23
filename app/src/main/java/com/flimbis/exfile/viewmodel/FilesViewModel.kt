package com.flimbis.exfile.viewmodel

import android.databinding.BaseObservable
import com.flimbis.exfile.domain.model.FileModel

class FilesViewModel(val fileModel: FileModel): BaseObservable() {
    fun getName(): String = fileModel.name
}