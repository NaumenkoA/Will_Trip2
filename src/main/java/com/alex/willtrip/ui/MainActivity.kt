package com.alex.willtrip.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.widget.Toast
import com.alex.willtrip.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.selectedItemId = R.id.action_habits

        navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_habits -> showToast ("Habit")
                R.id.action_today -> showToast ("Today")
                R.id.action_trip_mode -> showToast ("Will Trip")
                R.id.action_settings -> showFragment (FRAGMENT_SETTINGS)
            }
            true
        };
    }

    private fun showFragment(tag: String) {

        val fragment = supportFragmentManager.findFragmentByTag(tag)
        if (fragment != null) {
            supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
        } else
        {
            supportFragmentManager.beginTransaction().replace(R.id.container, createFragment(tag), tag).commit()
        }
    }

    private fun createFragment(tag: String): Fragment {
        return when (tag) {
            FRAGMENT_SETTINGS -> SettingsFragment()
            else -> throw IllegalArgumentException ("${this::class.java.simpleName}: can't create Fragment with tag: $tag. $tag is not found")
        }
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    companion object {
        const val FRAGMENT_SETTINGS = "fragment_settings"
    }
}
