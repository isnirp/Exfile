package com.flimbis.exfile

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

class AppPreference(private val context: Activity) {
    private val light = "LIGHT_THEME"
    private val dark = "DARK_THEME"
    private val pref: SharedPreferences = context?.getSharedPreferences("com.flimbis.exfile.apppref", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = pref.edit()

    fun setCustomTheme(theme: String) {
        editor.putString("theme", theme)
        editor.commit()
        context.recreate()
    }

    fun getCustomTheme(): Int {
        val theme = pref.getString("theme", light)
        return if (theme == dark) R.style.AppTheme_Dark
        else R.style.AppTheme_White
    }
}