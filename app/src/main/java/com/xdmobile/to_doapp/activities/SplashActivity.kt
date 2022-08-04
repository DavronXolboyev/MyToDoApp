package com.xdmobile.to_doapp.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.WindowInsets
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.xdmobile.to_doapp.MainActivity
import com.xdmobile.to_doapp.R
import com.xdmobile.to_doapp.database.DbConstants
import com.xdmobile.to_doapp.databinding.ActivitySplashBinding

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        val sharedPreferences = getSharedPreferences(DbConstants.Preference.NAME, MODE_PRIVATE)
        val isRegistered = sharedPreferences.getBoolean(DbConstants.Preference.KEY_REGISTER, false)
        val titles = listOf("To Do", "Funds monitoring", "Notes")
        var i = 0
        val anim = AnimationUtils.loadAnimation(this, R.anim.my_anim)
        object : CountDownTimer(2500, 820) {
            override fun onTick(millisUntilFinished: Long) {
                if (i < titles.size) {
                    binding.title.text = titles[i++]
                    binding.title.startAnimation(anim)
                }
            }

            override fun onFinish() {

            }

        }.start()

        Handler(mainLooper).postDelayed({
            if (!isRegistered)
                startActivity(Intent(this@SplashActivity, WelcomeActivity::class.java))
            else
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }, 2500)
    }
}