package com.flimbis.exfile.data

data class FileEntity(
        val path: String,
        val type: Int,
        val isWritable: Boolean,
        val name: String,
        val size: Long = 0,
        val ext: String?,
        val lastModified: Long?
)