package com.xdmobile.to_doapp.fragments.main_fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import com.xdmobile.to_doapp.activities.WelcomeActivity
import com.xdmobile.to_doapp.database.DbConstants.Preference
import com.xdmobile.to_doapp.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {
    private var binding: FragmentSettingsBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater)

        binding?.exitOnAccountButton?.setOnClickListener {
            activity?.getSharedPreferences(Preference.NAME, Context.MODE_PRIVATE)?.edit {
                putBoolean(Preference.KEY_REGISTER, false)
                apply()
            }
            startActivity(Intent(requireContext(), WelcomeActivity::class.java))
            activity?.finish()
        }


        return binding!!.root
    }
}