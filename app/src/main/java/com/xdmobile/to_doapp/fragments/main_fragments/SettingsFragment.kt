package com.xdmobile.to_doapp.fragments.main_fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.sqlite.SQLiteException
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.edit
import com.xdmobile.to_doapp.activities.WelcomeActivity
import com.xdmobile.to_doapp.database.CardDatabaseHelper
import com.xdmobile.to_doapp.database.DbConstants.Preference
import com.xdmobile.to_doapp.database.FinanceDatabaseHelper
import com.xdmobile.to_doapp.database.ToDoDatabaseHelper
import com.xdmobile.to_doapp.database.UserDatabaseHelper
import com.xdmobile.to_doapp.databinding.FragmentSettingsBinding
import com.xdmobile.to_doapp.model.UserModel

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding: FragmentSettingsBinding get() = _binding!!
    private var preferences: SharedPreferences? = null
    private val itsEmpty = "It's empty!"
    private lateinit var userDatabaseHelper: UserDatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        _binding = FragmentSettingsBinding.inflate(inflater)
        preferences = activity?.getSharedPreferences(Preference.NAME, Context.MODE_PRIVATE)
        userDatabaseHelper = UserDatabaseHelper(requireContext())

        initDataToViews()

        initListener()


        return binding.root
    }

    private fun initDataToViews() {
        val userId = preferences?.getInt(Preference.KEY_USER_ID, -1)
        val userModel = userDatabaseHelper.getUser(userId!!)

        if (userModel != null) {
            binding.settingsUsername.setText(userModel.username)
            binding.settingsEmail.setText(userModel.email)
            binding.settingsPassword.setText(userModel.password)
        }

    }

    private fun initListener() {
        binding.settingsLogOut.setOnClickListener {
            logOutOfProfile()
        }

        val userId = preferences?.getInt(Preference.KEY_USER_ID, -1)

        binding.settingsClearAllToDos.setOnClickListener {
            clearAllToDos(userId!!)
        }

        binding.settingsClearAllFinancialTransactions.setOnClickListener {
            clearAllFinancialTransactions(userId!!)
        }

        binding.settingsClearAllCards.setOnClickListener {
            clearAllCards(userId!!)
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

    private fun clearAllCards(userId: Int) {
        val cardDatabaseHelper = CardDatabaseHelper(requireContext())
        if (cardDatabaseHelper.deleteAllCardsWithUserId(userId)) {
            clearAllFinancialTransactions(userId)
            showToast("All cards have been deleted!")
        } else {
            showToast("Something went wrong...", false)
        }
    }

    private fun clearAllFinancialTransactions(userId: Int) {
        try {
            val financeDatabaseHelper = FinanceDatabaseHelper(requireContext())
            if (financeDatabaseHelper.deleteAllDataWithUserId(userId)) {
                showToast("All to-dos have been deleted!")
            } else
                showToast("Something went wrong...", false)
        } catch (e: SQLiteException) {
            e.stackTrace
        }
    }

    private fun updateUserInfo(userId: Int, username: String, email: String, password: String) {
        val newUser = UserModel(userId, username, email, password)
        if (userDatabaseHelper.updateUserData(newUser)) {
            showToast("Information has been updated")
        } else {
            showToast("Something went wrong...", false)
        }
        saveToSharedPreference(username)
    }

    private fun saveToSharedPreference(username: String) {
        val sharedPreferences = requireContext()
            .getSharedPreferences(
                Preference.NAME,
                Context.MODE_PRIVATE
            )
        sharedPreferences.edit().apply {
            putString(Preference.KEY_EMAIL_OR_USERNAME, username)
            apply()
        }
    }

    private fun clearAllToDos(userId: Int) {
        try {
            val toDoDatabaseHelper = ToDoDatabaseHelper(requireContext())
            if (toDoDatabaseHelper.deleteAllToDos(userId)) {
                showToast("All to-dos have been deleted!")
            } else
                showToast("Something went wrong...", false)
        } catch (e: SQLiteException) {
            e.stackTrace
        }
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

    private fun showToast(message: String, isShortTime: Boolean = true) {
        Toast.makeText(
            requireContext(),
            message,
            if (isShortTime) Toast.LENGTH_SHORT else Toast.LENGTH_LONG
        ).show()
    }

    override fun onDestroyView() {
        initDataToViews()
        _binding = null
        preferences = null
        super.onDestroyView()
    }
}