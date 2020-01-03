package com.flimbis.exfile.common

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class ExFileBroadcastReceiver(private val onChange: () -> Unit) : BroadcastReceiver() {
    companion object {
        const val DIR_UPDATE = "com.flimbis.exfile.DIR_UPDATE"
        const val DIR_PATH_KEY = "com.flimbis.exfile.DIR_PATH_KEY"
        const val BROADCAST_TYPE = "com.flimbis.exfile.BROADCAST_TYPE"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        //if (DIR_UPDATE == intent?.action) {
            onChange.invoke()
            Toast.makeText(context, "Broadcast Received", Toast.LENGTH_SHORT).show()
            Log.i("TAG_BROADCAST", "Broadcast Received")
        //}

    }
}