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
import com.xdmobile.to_doapp.database.DbConstants.Preference
import com.xdmobile.to_doapp.database.UserDatabaseHelper
import com.xdmobile.to_doapp.databinding.FragmentSignInBinding


class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding: FragmentSignInBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSignInBinding.inflate(inflater)

        binding.registrationButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.welcome_fragment_container, SignUpFragment()).commit()
        }

        binding.loginButton.setOnClickListener {
            logIn()
        }


        return binding.root
    }

    private fun logIn() {
        val userDatabaseHelper = UserDatabaseHelper(requireContext())
        val emailOrUsername = binding.signInUserEmail.text.toString().trim()
        val password = binding.signInPassword.text.toString().trim()
        if (emailOrUsername.isNotEmpty())
            if (password.isNotEmpty())
                if (userDatabaseHelper.isUserRegistered(emailOrUsername, password)) {
                    val sharedPreferences = requireContext().getSharedPreferences(
                        Preference.NAME,
                        Context.MODE_PRIVATE
                    )

                    val editor = sharedPreferences.edit()
                    editor.putBoolean(Preference.KEY_REGISTER, true)
                    val id = UserDatabaseHelper(requireContext()).getUserID(emailOrUsername)
                    if (id != null)
                        editor.putInt(Preference.KEY_USER_ID, id)
                    editor.putString(Preference.KEY_EMAIL_OR_USENAME, emailOrUsername)
                    editor.apply()

                    startActivity(Intent(requireContext(), MainActivity::class.java))
                    activity?.finish()
                } else
                    Toast.makeText(
                        requireContext(),
                        "There is an error in the email or password",
                        Toast.LENGTH_LONG
                    ).show()
            else
                binding.signInPassword.error = "It's empty"
        else
            binding.signInUserEmail.error = "It's empty"

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}