package com.flimbis.exfile.model

data class FileModel(val path: String,
                     val type: String = "folder",
                     val name: String,
                     val size: Double = 0.0,
                     val ext: String)