package com.flimbis.exfile.util

import java.io.File

fun getFilesFromPath(path: String): List<File> {
    val file = File(path)//Creates a new File instance by converting the given pathname string into an abstract pathname.
    //listFiles(); Returns an array of abstract path names denoting the files in the directory denoted by this abstract pathname.
    return file.listFiles().toList()
}

fun convertFileSizeToMB(sizeInBytes: Long): Double {
    return (sizeInBytes.toDouble()) / (1024 * 1024)
}