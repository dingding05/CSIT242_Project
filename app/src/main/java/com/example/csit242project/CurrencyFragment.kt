package com.example.csit242project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ListView
import androidx.fragment.app.Fragment

class CurrencyFragment : Fragment() {

    private lateinit var settingsManager: SettingsManager
    private val currencies = arrayOf("AED - UAE Dirham", "USD - US Dollar", "EUR - Euro", "GBP - British Pound", "INR - Indian Rupee", "SAR - Saudi Riyal")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings_currency, container, false)
        settingsManager = SettingsManager(requireContext())

        val lvCurrency = view.findViewById<ListView>(R.id.lvCurrency)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_single_choice, currencies)
        lvCurrency.adapter = adapter
        lvCurrency.choiceMode = ListView.CHOICE_MODE_SINGLE

        // Select current currency
        val currentCurrency = settingsManager.currency
        val currentIndex = currencies.indexOfFirst { it.startsWith(currentCurrency) }
        if (currentIndex != -1) {
            lvCurrency.setItemChecked(currentIndex, true)
        }

        lvCurrency.setOnItemClickListener { _, _, position, _ ->
            val selected = currencies[position].split(" - ")[0]
            settingsManager.currency = selected
        }

        view.findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return view
    }
}
