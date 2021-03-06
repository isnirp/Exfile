package com.flimbis.exfile.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import android.util.Log
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

//For android version 23 or higher, you need to give run time permission programmatically
//https://stackoverflow.com/questions/20714058/file-exists-and-is-directory-but-listfiles-returns-null
fun getFilesFromPath(path: String): List<File> {
    //external storage /storage/emulated/0
    Log.i("TAG_FILE_PATH", path)
    val file = File(path)//Creates a new File instance by converting the given pathname string into an abstract pathname.
    //listFiles(); Returns an array of abstract path names denoting the files in the directory denoted by this abstract pathname.
    return file.listFiles().toList()
}

fun getFileFromPath(path: String): File = File(path)

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

fun copyFileToDirectory(contentUri: Uri, context: Context) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip: ClipData = ClipData.newUri(context.contentResolver, "URI", contentUri)
    clipboard.primaryClip = clip
}

fun renameFileAtDirectory(dirPath: String, prevName: String, curName: String): Boolean {
    val directory = File(dirPath)
    val fileToRename = File(directory, prevName)
    val newFile = File(directory, curName)

    //Renames the file denoted by this abstract pathname
    return fileToRename.renameTo(newFile)

}

fun convertLastModified(lastModified: Long): String {
    return SimpleDateFormat("dd-MM-yyyy").format(Date(lastModified))
}