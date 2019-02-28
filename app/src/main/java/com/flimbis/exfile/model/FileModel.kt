package com.flimbis.exfile.model

data class FileModel(val path: String,
                     val type: String,
                     val name: String,
                     val size: Double,
                     val extension: String)