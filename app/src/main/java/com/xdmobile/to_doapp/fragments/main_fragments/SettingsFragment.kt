package com.xdmobile.to_doapp.fragments.main_fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.edit
import com.xdmobile.to_doapp.activities.WelcomeActivity
import com.xdmobile.to_doapp.database.DbConstants.Preference
import com.xdmobile.to_doapp.database.ToDoDatabaseHelper
import com.xdmobile.to_doapp.database.UserDatabaseHelper
import com.xdmobile.to_doapp.databinding.FragmentSettingsBinding
import com.xdmobile.to_doapp.model.UserModel
import java.util.prefs.Preferences

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding: FragmentSettingsBinding get() = _binding!!
    private var preferences: SharedPreferences? = null
    private val itsEmpty = "It's empty!"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        _binding = FragmentSettingsBinding.inflate(inflater)
        preferences = activity?.getSharedPreferences(Preference.NAME, Context.MODE_PRIVATE)


        initListener()


        return binding.root
    }

    private fun initListener() {
        binding.settingsLogOut.setOnClickListener {
            logOutOfProfile()
        }

        val userId = preferences?.getInt(Preference.KEY_USER_ID, -1)

        binding.settingsClearAllToDos.setOnClickListener {
            clearAllToDos(userId!!)
        }

        binding.settingsEditButton.setOnClickListener {
            setEnabledToViews(true)

        }

        binding.settingsSaveButton.setOnClickListener {
            with(binding) {
                val username = binding.settingsUsername.text.toString().trim()
                if (username.isNotEmpty()) {
                    val email = settingsEmail.text.toString().trim()
                    if (email.isNotEmpty()) {
                        val password = settingsPassword.text.toString().trim()
                        if (password.isNotEmpty()) {
                            updateUserInfo(userId!!, username, email, password)
                            setEnabledToViews(false)
                        } else {
                            settingsPassword.error = itsEmpty
                        }
                    } else {
                        settingsEmail.error = itsEmpty
                    }
                } else {
                    settingsUsername.error = itsEmpty
                }
            }
        }
    }

    private fun updateUserInfo(userId: Int, username: String, email: String, password: String) {
        val userDatabaseHelper = UserDatabaseHelper(requireContext())
        val newUser = UserModel(userId, username, email, password)
        if (userDatabaseHelper.updateUserData(newUser)) {
            Toast.makeText(requireContext(), "Information has been updated", Toast.LENGTH_LONG)
                .show()
        } else {
            Toast.makeText(requireContext(), "Something went wrong...", Toast.LENGTH_LONG).show()
        }
    }

    private fun clearAllToDos(userId: Int) {
        val toDoDatabaseHelper = ToDoDatabaseHelper(requireContext())
        if (toDoDatabaseHelper.deleteAllToDos(userId)) {
            Toast.makeText(requireContext(), "All to-dos have been deleted!", Toast.LENGTH_LONG)
                .show()
        } else
            Toast.makeText(requireContext(), "Something went wrong...", Toast.LENGTH_LONG).show()
    }

    private fun logOutOfProfile() {
        activity?.getSharedPreferences(Preference.NAME, Context.MODE_PRIVATE)?.edit {
            putBoolean(Preference.KEY_REGISTER, false)
            apply()
        }
        startActivity(Intent(requireContext(), WelcomeActivity::class.java))
        activity?.finish()
    }

    private fun setEnabledToViews(isEnabled: Boolean) {
        binding.settingsUsername.isEnabled = isEnabled
        binding.settingsEmail.isEnabled = isEnabled
        binding.settingsPassword.isEnabled = isEnabled
        binding.settingsSaveButton.visibility = if (isEnabled) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        preferences = null
    }
}