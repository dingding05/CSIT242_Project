package com.example.csit242project

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.Locale

object LocaleHelper {
    fun applyLocale(context: Context) {
        val settingsManager = SettingsManager(context)
        val language = settingsManager.language
        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(language)
        AppCompatDelegate.setApplicationLocales(appLocale)
    }

    // Still useful for older versions or specific context updates
    fun updateBaseContextLocale(context: Context): Context {
        val settingsManager = SettingsManager(context)
        val language = settingsManager.language
        val locale = Locale(language)
        Locale.setDefault(locale)

        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)

        return context.createConfigurationContext(configuration)
    }
}
