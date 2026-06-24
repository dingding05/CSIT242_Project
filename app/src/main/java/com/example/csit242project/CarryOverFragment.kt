package com.example.csit242project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.google.android.material.materialswitch.MaterialSwitch

class CarryOverFragment : Fragment() {

    private lateinit var settingsManager: SettingsManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings_carryover, container, false)
        settingsManager = SettingsManager(requireContext())

        val swCarryover = view.findViewById<MaterialSwitch>(R.id.swCarryover)
        swCarryover.isChecked = settingsManager.isCarryoverEnabled

        swCarryover.setOnCheckedChangeListener { _, isChecked ->
            settingsManager.isCarryoverEnabled = isChecked
        }

        view.findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return view
    }
}
