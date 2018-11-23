package com.flimbis.exfile.domain

import com.flimbis.exfile.domain.model.FileModel

interface FileInteractor {
    fun getFiles(): List<FileModel>

    fun delete(path: String): Int
}