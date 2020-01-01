package com.flimbis.exfile.service

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import com.flimbis.exfile.common.ExFileBroadcastReceiver

class ExCopyJobService: JobService() {
    override fun onStopJob(params: JobParameters?): Boolean {
        return false
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        return true
    }

    private fun sendBroadcastToApp(path: String) {
        val intent = Intent(ExFileBroadcastReceiver.DIR_UPDATE)
        intent.putExtra(ExFileBroadcastReceiver.DIR_PATH_KEY, path)
        sendBroadcast(intent)
    }
}