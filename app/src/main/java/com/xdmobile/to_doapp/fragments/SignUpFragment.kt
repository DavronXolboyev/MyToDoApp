package com.xdmobile.to_doapp.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.xdmobile.to_doapp.MainActivity
import com.xdmobile.to_doapp.R
import com.xdmobile.to_doapp.database.DbConstants
import com.xdmobile.to_doapp.database.UserDatabaseHelper
import com.xdmobile.to_doapp.databinding.FragmentSignUpBinding
import com.xdmobile.to_doapp.model.UserModel

class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding : FragmentSignUpBinding get() = _binding!!
    private val itsEmpty = "It's empty!"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        _binding = FragmentSignUpBinding.inflate(inflater)

        with(binding) {

            loginButton.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.welcome_fragment_container, SignInFragment()).commit()
            }

            registerButton.setOnClickListener {
                userRegistration()
            }

        }

        return binding.root
    }

    private fun userRegistration() {
        with(binding) {
            val username = signUpUsername.text.toString().trim()
            val email = signUpEmail.text.toString().trim()
            val password = signUpPassword.text.toString()
            val confirmPassword = signUpPasswordConfirm.text.toString()
            if (username.isNotEmpty()) {
                if (email.isNotEmpty()) {
                    if (password.isNotEmpty()) {
                        if (confirmPassword.isNotEmpty()) {
                            if (password == confirmPassword) {
                                if (isUserRegistered(username, email, password)) {
                                    completeTheProcess(email)
                                }
                            } else {
                                showToast("There are two types of passwords entered", true)
                            }
                        } else {
                            signUpPasswordConfirm.error = itsEmpty
                        }
                    } else {
                        signUpPassword.error = itsEmpty
                    }
                } else {
                    signUpEmail.error = itsEmpty
                }
            } else {
                signUpUsername.error = itsEmpty
            }
        }
    }

    private fun isUserRegistered(username: String, email: String, password: String): Boolean {
        val userDatabaseHelper = UserDatabaseHelper(requireContext())
        if (userDatabaseHelper.isHaveUsername(username)) {
            showToast("Such username already exists")
            return false
        }
        if (userDatabaseHelper.isHaveEmail(email)) {
            showToast("Such email already exists")
            return false
        }
        val user = UserModel(username, email, password)
        val isProcessCompleted = userDatabaseHelper.insertUserData(user)
        if (!isProcessCompleted) {
            showToast("Something went wrong...", true)
            return isProcessCompleted
        }
        return isProcessCompleted
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun showToast(text: String, isLong: Boolean = false) {
        Toast.makeText(
            requireContext(),
            text,
            if (isLong)
                Toast.LENGTH_LONG
            else
                Toast.LENGTH_SHORT
        ).show()
    }

    private fun completeTheProcess(email: String) {
        val sharedPreferences = requireContext()
            .getSharedPreferences(
                DbConstants.Preference.NAME,
                Context.MODE_PRIVATE
            )

        sharedPreferences.edit().apply {
            putBoolean(DbConstants.Preference.KEY_REGISTER, true)
            val id = UserDatabaseHelper(requireContext()).getUserID(email)
            if (id != null)
                putInt(DbConstants.Preference.KEY_USER_ID, id)
            putString(DbConstants.Preference.KEY_EMAIL_OR_USERNAME,email)
            apply()
        }

        showToast("Process completed successfully")

        startActivity(
            Intent(
                requireContext(),
                MainActivity::class.java
            )
        )

        activity?.finish()
    }
}