package com.flimbis.exfile.util

import java.io.File

fun getFilesFromPath(path: String): List<File> {
    val file = File(path)//Creates a new File instance by converting the given pathname string into an abstract pathname.
    //listFiles(); Returns an array of abstract path names denoting the files in the directory denoted by this abstract pathname.
    return file.listFiles().toList()
}

fun createFolderAtDirectory(path: String, folderName: String): Boolean {
    val file = File(path, folderName)//Creates a new File instance from a parent pathname string and a child pathname string
    return try {
        file.mkdir()
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

fun createFileAtDirectory(path: String, fileName: String): Boolean {
    val file = File(path, fileName)
    return try {
        file.createNewFile()
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

fun convertFileSizeToMB(sizeInBytes: Long): Double {
    return (sizeInBytes.toDouble()) / (1024 * 1024)
}