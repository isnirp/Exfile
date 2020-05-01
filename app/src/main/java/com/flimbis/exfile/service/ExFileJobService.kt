package com.flimbis.exfile.service

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.flimbis.exfile.common.ExFileBroadcastReceiver
import com.flimbis.exfile.util.createFileAtDirectory
import com.flimbis.exfile.util.createFolderAtDirectory

class ExFileJobService : JobService() {
    override fun onStopJob(params: JobParameters?): Boolean {
        return false
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        //Toast.makeText(this, "ExFileJobService", Toast.LENGTH_SHORT).show()
        val bundle = params?.extras
        val path = bundle?.getString("FILE_PATH")
        val name = bundle?.getString("FILE_NAME")
        val fileType = bundle?.getString("FILE_TYPE")


        Log.i("TAG_SERVICE", "Service Started")

        if (fileType == "FOLDER") {
            if (createFolderAtDirectory(path!!, name!!)) sendBroadcastToApp(path)
        } else {
            if (createFileAtDirectory(path!!, name!!)) sendBroadcastToApp(path)
        }

        jobFinished(params, false)

        //if (params.triggeredContentAuthorities != null) {
        //sendBroadcastToApp
        //  jobFinished(params, false)
        //}

        return true
    }

    private fun sendBroadcastToApp(path: String) {
        val intent = Intent(ExFileBroadcastReceiver.DIR_UPDATE)
        intent.putExtra(ExFileBroadcastReceiver.DIR_PATH_KEY, path)
        sendBroadcast(intent)
    }
}