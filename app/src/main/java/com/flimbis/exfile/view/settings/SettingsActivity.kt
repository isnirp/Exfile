package com.flimbis.exfile.view.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.flimbis.exfile.R

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.content_settings, SettingsFragment())
                .commit()
    }
}
