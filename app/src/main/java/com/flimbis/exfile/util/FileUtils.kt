package com.flimbis.exfile.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.FileProvider
import java.io.File

//For android version 23 or higher, you need to give run time permission programmatically
//https://stackoverflow.com/questions/20714058/file-exists-and-is-directory-but-listfiles-returns-null
fun getFilesFromPath(path: String): List<File> {
    //external storage /storage/emulated/0
    Log.i("TAG_FILE_PATH", path)
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

fun deleteFileAtPath(path: String): Boolean {
    val file = File(path)
    return file.delete()
}

//directory can only be deleted if it's empty
fun deleteDirectory(path: String): Boolean {
    val file = File(path)
    return file.deleteRecursively()
}

fun convertFileSizeToMB(sizeInBytes: Long): Double {
    return (sizeInBytes.toDouble()) / (1024 * 1024)
}

fun copyFileToDirectory(contentUri: Uri, context: Context){
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip: ClipData = ClipData.newUri(context.contentResolver, "URI", contentUri)
    clipboard.primaryClip = clip
}