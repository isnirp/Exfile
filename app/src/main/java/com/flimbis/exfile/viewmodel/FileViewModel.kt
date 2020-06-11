package com.flimbis.exfile.viewmodel

import androidx.databinding.BaseObservable
import com.flimbis.exfile.data.DataRepository
import com.flimbis.exfile.data.FileEntity
import com.flimbis.exfile.model.FileModel
import com.flimbis.exfile.util.convertFileSizeToMB
import com.flimbis.exfile.util.convertLastModified

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
                fileEntity.size.toDouble(),
                fileEntity.ext,
                convertLastModified(fileEntity.lastModified)
        )
    }

    private fun getFileType(type: Int): String {
        return if (type == 0) "folder" else "file"
    }
}