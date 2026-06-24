package com.example.csit242project

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNav = findViewById(R.id.bottomNav)

        // Show the Dashboard the first time the activity is created.
        if (savedInstanceState == null) {
            showFragment(DashboardFragment())
        }

        bottomNav.setOnItemSelectedListener { item ->
            val fragment = when (item.itemId) {
                R.id.nav_dashboard -> DashboardFragment()
                R.id.nav_history -> HistoryFragment()
                R.id.nav_profile -> ProfileFragment()
                R.id.nav_settings -> SettingsFragment()
                else -> null
            }
            if (fragment != null) {
                showFragment(fragment)
                true
            } else {
                false
            }
        }
    }

    private fun showFragment(fragment: Fragment) {
        // Clear any settings sub-screens that may be on the back stack
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    /** Called by DashboardFragment's "See All" link. */
    fun navigateToHistory() {
        bottomNav.selectedItemId = R.id.nav_history
    }

    /** Called by ProfileFragment's settings card. */
    fun navigateToSettings() {
        bottomNav.selectedItemId = R.id.nav_settings
    }
}
