package com.example.csit242project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        view.findViewById<LinearLayout>(R.id.row_currency).setOnClickListener {
            navigateToSubSetting(CurrencyFragment())
        }

        view.findViewById<LinearLayout>(R.id.row_theme).setOnClickListener {
            navigateToSubSetting(ThemeFragment())
        }

        view.findViewById<LinearLayout>(R.id.row_categories).setOnClickListener {
            navigateToSubSetting(CategoriesFragment())
        }

        view.findViewById<LinearLayout>(R.id.row_carryover).setOnClickListener {
            navigateToSubSetting(CarryOverFragment())
        }

        view.findViewById<LinearLayout>(R.id.row_language).setOnClickListener {
            navigateToSubSetting(LanguageFragment())
        }

        return view
    }

    private fun navigateToSubSetting(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}
