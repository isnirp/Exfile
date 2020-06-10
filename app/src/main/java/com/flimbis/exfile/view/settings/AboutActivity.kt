package com.flimbis.exfile.view.settings

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.flimbis.exfile.BaseActivity
import com.flimbis.exfile.R


class AboutActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        val toolbar = findViewById<Toolbar>(R.id.toolbar_about)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.title = "About"

        val version = findViewById<TextView>(R.id.txt_version)
        version.text = "version - " + getVersionName(this)

        val privacy = findViewById<Button>(R.id.bttn_privacy)
        privacy.setOnClickListener {
            val url = "https://isnirp.github.io/flimbis-apps/privacy/exfile.html"
            val intnt = Intent(Intent.ACTION_VIEW)
            intnt.data = Uri.parse(url)
            startActivity(intnt)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun getVersionName(context: Context): String? {
        return try {
            val pInfo: PackageInfo = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA)
            pInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            null
        }
    }

    fun getVersionCode(context: Context): Int {
        return try {
            val pInfo = context.packageManager.getPackageInfo(
                    context.packageName, PackageManager.GET_META_DATA)
            pInfo.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            0
        }
    }
}
