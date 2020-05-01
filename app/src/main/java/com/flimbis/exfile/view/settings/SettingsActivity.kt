package com.flimbis.exfile.view.settings

import android.os.Bundle
import com.flimbis.exfile.BaseActivity
import com.flimbis.exfile.R

class SettingsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.content_settings, SettingsFragment())
                .commit()
    }
}
