package com.flimbis.exfile

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity

/*
* Other activities extend this
* */
open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setAppTheme()
        super.onCreate(savedInstanceState)
    }

    private fun setAppTheme() {
        setTheme(AppPreference(this).getCustomTheme())
    }
}