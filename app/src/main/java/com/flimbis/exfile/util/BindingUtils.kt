package com.flimbis.exfile.util

import android.graphics.BitmapFactory
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.flimbis.exfile.R

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

@BindingAdapter(value = ["loadImage", "ext"])
fun renderImages(v: ImageView, img: String, ext: String?) {
    val mimeFilter = listOf(
            "jpeg",
            "jpg",
            "png",
            "gif"
    )
    if (ext in mimeFilter) {
        v.setImageBitmap(BitmapFactory.decodeFile(img))
    } else {
        if (null != ext && "" != ext)
            v.setImageResource(R.drawable.ic_drive_file)
        else
            v.setImageResource(R.drawable.ic_folder)
    }

}