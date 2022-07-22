package com.xdmobile.to_doapp.fragments

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
import com.xdmobile.to_doapp.databinding.FragmentSignInBinding


class SignInFragment : Fragment() {

    private var binding: FragmentSignInBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentSignInBinding.inflate(inflater)
        val userDatabaseHelper = UserDatabaseHelper(requireContext())
        with(binding!!) {

            registrationButton.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.welcome_fragment_container, SignUpFragment()).commit()
            }

            loginButton.setOnClickListener {
                val username = signInUsername.text.toString()
                val password = signInPassword.text.toString()
                if (username.isNotEmpty())
                    if (password.isNotEmpty())
                        if (userDatabaseHelper.isUserRegistered(username, password)) {
                            val sharedPreferences = requireContext().getSharedPreferences(
                                DbConstants.Preference.NAME,
                                0x0000)
                            val editor = sharedPreferences.edit()
                            editor.putBoolean(DbConstants.Preference.KEY_REGISTER, true)
                            editor.apply()
                            startActivity(Intent(requireContext(), MainActivity::class.java))
                            activity?.finish()
                        } else
                            Toast.makeText(requireContext(),
                                "There is an error in the username or password",
                                Toast.LENGTH_LONG).show()
                    else
                        signInPassword.error = "It's empty"
                else
                    signInUsername.error = "It's empty"

            }
        }


        return binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}