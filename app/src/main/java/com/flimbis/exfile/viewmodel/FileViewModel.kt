package com.flimbis.exfile.viewmodel

import androidx.databinding.BaseObservable
import com.flimbis.exfile.data.DataRepository
import com.flimbis.exfile.data.FileEntity
import com.flimbis.exfile.model.FileModel
import com.flimbis.exfile.util.convertFileSizeToMB

class FileViewModel(private val repository: DataRepository) : BaseObservable() {
    fun getFiles(path: String): List<FileModel> {
        return repository.getFileEntityList(path)
                .map { toModel(it) }
    }

    private fun toModel(fileEntity: FileEntity): FileModel {
        return FileModel(
                fileEntity.path,
                getFileType(fileEntity.type),
                fileEntity.isWritable,
                fileEntity.name,
                convertFileSizeToMB(fileEntity.size),
                fileEntity.ext,
                null
        )
    }

    private fun getFileType(type: Int): String {
        return if (type == 0) "folder" else "file"
    }
}