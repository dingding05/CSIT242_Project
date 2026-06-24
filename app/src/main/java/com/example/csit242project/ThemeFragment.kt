package com.example.csit242project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment

class ThemeFragment : Fragment() {

    private lateinit var settingsManager: SettingsManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings_theme, container, false)
        settingsManager = SettingsManager(requireContext())

        val rgTheme = view.findViewById<RadioGroup>(R.id.rgTheme)
        
        // Set initial state
        when (settingsManager.themeMode) {
            SettingsManager.THEME_LIGHT -> rgTheme.check(R.id.rbLight)
            SettingsManager.THEME_DARK -> rgTheme.check(R.id.rbDark)
            SettingsManager.THEME_SYSTEM -> rgTheme.check(R.id.rbSystem)
        }

        rgTheme.setOnCheckedChangeListener { _, checkedId ->
            val mode = when (checkedId) {
                R.id.rbLight -> SettingsManager.THEME_LIGHT
                R.id.rbDark -> SettingsManager.THEME_DARK
                else -> SettingsManager.THEME_SYSTEM
            }
            settingsManager.themeMode = mode
        }

        view.findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return view
    }
}
