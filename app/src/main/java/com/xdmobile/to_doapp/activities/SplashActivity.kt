package com.xdmobile.to_doapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.xdmobile.to_doapp.MainActivity
import com.xdmobile.to_doapp.R
import com.xdmobile.to_doapp.database.DbConstants

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val sharedPreferences = getSharedPreferences(DbConstants.Preference.NAME, MODE_PRIVATE)
        val isRegistered = sharedPreferences.getBoolean(DbConstants.Preference.KEY_REGISTER, false)
        Handler(mainLooper).postDelayed({
            if (!isRegistered)
                startActivity(Intent(this@SplashActivity, WelcomeActivity::class.java))
            else
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }, 1500)
    }
}