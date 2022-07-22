package com.xdmobile.to_doapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.xdmobile.to_doapp.databinding.ActivityMainBinding
import com.xdmobile.to_doapp.fragments.main_fragments.CalendarFragment
import com.xdmobile.to_doapp.fragments.main_fragments.FinanceFragment
import com.xdmobile.to_doapp.fragments.main_fragments.SettingsFragment
import com.xdmobile.to_doapp.fragments.main_fragments.ToDoListFragment

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        changeFragment(CalendarFragment())
        binding.bottomNav.setOnItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.menu_calendar -> {
                changeFragment(CalendarFragment())
                return true
            }
            R.id.menu_to_do -> {
                changeFragment(ToDoListFragment())
                return true
            }
            R.id.menu_finance -> {
                changeFragment(FinanceFragment())
                return true
            }
            else -> {
                changeFragment(SettingsFragment())
                return true
            }
        }

        return false
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.main_fragment_container, fragment)
            .commit()
    }
}