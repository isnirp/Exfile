package com.flimbis.exfile

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity

/*
* Other activities extend this
* */
open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        setAppTheme()
        super.onCreate(savedInstanceState, persistentState)
    }

    fun setAppTheme() {
        setTheme(AppPreference(this).getCustomTheme())
    }
}