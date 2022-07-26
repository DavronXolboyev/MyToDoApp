package com.xdmobile.to_doapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.xdmobile.to_doapp.database.DbConstants
import com.xdmobile.to_doapp.database.UserDatabaseHelper
import com.xdmobile.to_doapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNav.setupWithNavController(navController)

        val email = getSharedPreferences(
            DbConstants.Preference.NAME,
            MODE_PRIVATE
        ).getString(DbConstants.Preference.KEY_EMAIL_OR_USENAME, "")

        val id = UserDatabaseHelper(this).getUserID(email!!)

        Toast.makeText(applicationContext, "$email || $id", Toast.LENGTH_LONG).show()

    }
}