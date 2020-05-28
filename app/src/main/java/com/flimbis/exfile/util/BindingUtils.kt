package com.flimbis.exfile.util

fun displaySize(size: Double): String {
    val kb = 1024
    val mb = 1024 * kb
    val gb = 1024 * mb

    return when {
        size * gb > gb -> "${String.format("%.2f", size)} gb"
        size * mb > mb -> "${String.format("%.2f", size)} mb"
        else -> "${String.format("%.2f", size)} kb"
    }

}