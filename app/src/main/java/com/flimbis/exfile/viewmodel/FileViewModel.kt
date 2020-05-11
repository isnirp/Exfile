package com.flimbis.exfile.viewmodel

import androidx.databinding.BaseObservable
import com.flimbis.exfile.data.DataRepository
import com.flimbis.exfile.model.FileModel

class FileViewModel(private val repository: DataRepository) : BaseObservable() {
    fun getFiles(path: String): List<FileModel>{
        //Todo map FileEntity to FileModel
    }
}