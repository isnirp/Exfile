package com.flimbis.exfile.viewmodel

import androidx.databinding.BaseObservable
import com.flimbis.exfile.model.FileModel
import com.flimbis.exfile.util.convertLastModified
import com.flimbis.exfile.util.getFilesFromPath
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class FileViewModel() : BaseObservable() {

    suspend fun getFiles(path: String): List<FileModel> {
        return withContext(Dispatchers.IO) {
            getFilesFromPath(path)
                    .map { toModel(it) }
        }
    }

    private fun toModel(file: File): FileModel {
        return FileModel(
                file.path,
                getFileType(file.isDirectory),
                file.canWrite(),
                file.name,
                file.length().toDouble(),
                file.extension,
                convertLastModified(file.lastModified())
        )
    }

    private fun getFileType(isDir: Boolean): String {
        return if (isDir) "folder" else "file"
    }
}