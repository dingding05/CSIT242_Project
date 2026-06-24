package com.example.csit242project

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class SettingsManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)

    companion object {
        const val KEY_THEME = "theme_mode"
        const val KEY_CURRENCY = "currency_code"
        const val KEY_LANGUAGE = "language_code"
        const val KEY_CARRYOVER = "carryover_enabled"

        const val THEME_LIGHT = 0
        const val THEME_DARK = 1
        const val THEME_SYSTEM = 2
    }

    var themeMode: Int
        get() = prefs.getInt(KEY_THEME, THEME_SYSTEM)
        set(value) {
            prefs.edit().putInt(KEY_THEME, value).apply()
            applyTheme(value)
        }

    var currency: String
        get() = prefs.getString(KEY_CURRENCY, "AED") ?: "AED"
        set(value) = prefs.edit().putString(KEY_CURRENCY, value).apply()

    var language: String
        get() = prefs.getString(KEY_LANGUAGE, "en") ?: "en"
        set(value) = prefs.edit().putString(KEY_LANGUAGE, value).apply()

    var isCarryoverEnabled: Boolean
        get() = prefs.getBoolean(KEY_CARRYOVER, false)
        set(value) = prefs.edit().putBoolean(KEY_CARRYOVER, value).apply()

    fun applyTheme(mode: Int) {
        when (mode) {
            THEME_LIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            THEME_DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            THEME_SYSTEM -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }
}
