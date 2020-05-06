package com.flimbis.exfile.view.settings

import android.content.Intent
import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.flimbis.exfile.AppPreference
import com.flimbis.exfile.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        val themePreference = findPreference<ListPreference>("themePref")
        themePreference?.setOnPreferenceChangeListener { preference, newValue ->
            AppPreference(activity!!).setCustomTheme(newValue.toString())
            true
        }

        val aboutPreference = findPreference<Preference>("aboutPref")
        val aboutIntent = Intent(context, AboutActivity::class.java)
        aboutPreference?.intent = aboutIntent
    }
}