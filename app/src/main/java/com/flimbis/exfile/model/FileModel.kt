package com.flimbis.exfile.model

data class FileModel(val path: String,
                     val isDirectory: Boolean,
                     val name: String,
                     val size: Long = 0,
                     val ext: String,
                     val lastModified: Long)

enum class FileType(val type: String){
    DIRECTORY("FOLDER"),
    FILE("FILE")
}