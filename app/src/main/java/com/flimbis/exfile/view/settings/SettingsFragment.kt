package com.flimbis.exfile.view.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.flimbis.exfile.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}