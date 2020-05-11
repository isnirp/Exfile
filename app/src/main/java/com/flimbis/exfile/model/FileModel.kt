package com.flimbis.exfile.model

import java.util.*

data class FileModel(val path: String,
                     val type: String,
                     val isWritable: Boolean,
                     val name: String,
                     val size: Double = 0.0,
                     val ext: String?,
                     val lastModified: Date?)
