package com.flimbis.exfile.domain.model

data class FileModel(val path: String,
                     val type: String,
                     val name: String,
                     val size: Double,
                     val extension: String)