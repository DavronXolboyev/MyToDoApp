package com.xdmobile.to_doapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.xdmobile.to_doapp.R
import com.xdmobile.to_doapp.databinding.ActivityWelcomeBinding
import com.xdmobile.to_doapp.fragments.SignInFragment

class WelcomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .replace(R.id.welcome_fragment_container, SignInFragment()).commit()
    }
}