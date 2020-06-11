package com.flimbis.exfile.util

fun displaySize(sizeInBytes: Double): String {
    //val kb = 1024.0

    val sizeKB = sizeInBytes / 1024.0
    val sizeMB = sizeInBytes / (1024.0 * 1024.0)

    //"${String.format("%.2f", sizeKB)} kb"
    return if (sizeInBytes < (1024 * 1024))
        String.format("%.2f", sizeKB) + " kb"
    else
        String.format("%.2f", sizeMB) + " mb"
}