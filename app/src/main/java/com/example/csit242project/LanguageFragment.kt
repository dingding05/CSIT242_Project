package com.example.csit242project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ListView
import androidx.fragment.app.Fragment

class LanguageFragment : Fragment() {

    private lateinit var settingsManager: SettingsManager
    private val languages = arrayOf("English", "Arabic", "French", "Spanish", "German", "Hindi")
    private val codes = arrayOf("en", "ar", "fr", "es", "de", "hi")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings_language, container, false)
        settingsManager = SettingsManager(requireContext())

        val lvLanguage = view.findViewById<ListView>(R.id.lvLanguage)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_single_choice, languages)
        lvLanguage.adapter = adapter
        lvLanguage.choiceMode = ListView.CHOICE_MODE_SINGLE

        // Select current language
        val currentCode = settingsManager.language
        val currentIndex = codes.indexOf(currentCode)
        if (currentIndex != -1) {
            lvLanguage.setItemChecked(currentIndex, true)
        }

        lvLanguage.setOnItemClickListener { _, _, position, _ ->
            val selectedCode = codes[position]
            if (settingsManager.language != selectedCode) {
                settingsManager.language = selectedCode
                // Apply the new locale immediately
                // This will trigger an activity recreation with the new language
                LocaleHelper.applyLocale(requireContext())
            }
        }

        view.findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return view
    }
}
