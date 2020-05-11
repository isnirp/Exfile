package com.flimbis.exfile.data

import java.io.File

class DataRepository {
    fun getFileEntityList(path: String): List<FileEntity> {
        val file = File(path)//Creates a new File instance by converting the given pathname string into an abstract pathname.
        //listFiles(); Returns an array of abstract path names denoting the files in the directory denoted by this abstract pathname.
        return file.listFiles().toList()
                .map {
                    FileEntity(path = it.path, type = getFileType(it.isDirectory), isWritable = it.canWrite(),
                            name = it.name, size = it.length(), ext = it.extension, lastModified = it.lastModified())
                }
    }

    private fun getFileType(isDirectory: Boolean): Int {
        return if (isDirectory) 0 else 1
    }
}