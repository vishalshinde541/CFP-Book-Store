package com.example.bookstore.view

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.bookstore.R
import com.example.bookstore.databinding.FragmentLoginBinding
import com.example.bookstore.databinding.FragmentRegisterBinding
import com.example.bookstore.mode.User
import com.example.bookstore.mode.UserAuthService
import com.example.bookstore.viewmodel.LoginViewModel
import com.example.bookstore.viewmodel.LoginViewModelFactory


class LoginFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel
    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loginViewModel = ViewModelProvider(this, LoginViewModelFactory(UserAuthService())).get(
            LoginViewModel::class.java
        )

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
//        (activity as MainActivity?)?.setDrawerLocked()

        binding.forgotpass.setOnClickListener {
            val fragment = ResetPasswordFragment()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragmentsContainer, fragment)?.commit()
            transaction?.addToBackStack(null)
        }

        binding.tvNewResister.setOnClickListener {
            val fragment = RegisterFragment()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragmentsContainer, fragment)?.commit()
            transaction?.addToBackStack(null)
        }

        binding.loginBtn.setOnClickListener {
            validateField()
        }



        return view
    }

    private fun firebaseSignIn() {
        binding.loginBtn.isEnabled = false
        binding.loginBtn.alpha = 0.5f

        var user = User(
            email = binding.loginUsername.text.toString(),
            password = binding.loginPassword.text.toString()
        )
        loginViewModel.loginUser(user)
        loginViewModel._userLoginStatus.observe(viewLifecycleOwner, Observer {

            if (it.status) {
                val fragment = HomeFragment()
                val transaction = fragmentManager?.beginTransaction()
                transaction?.replace(R.id.fragmentsContainer, fragment)?.disallowAddToBackStack()
                    ?.commit()
            } else {
                binding.loginBtn.isEnabled = true
                binding.loginBtn.alpha = 1.0f
                Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun validateField() {
        val icon =
            AppCompatResources.getDrawable(requireContext(), R.drawable.ic_baseline_warning_24)
        icon?.setBounds(0, 0, icon.intrinsicWidth, icon.intrinsicHeight)

        when {
            TextUtils.isEmpty(binding.loginUsername.text.toString().trim()) -> {
                binding.loginUsername.setError("Please Enter the username", icon)
            }
            TextUtils.isEmpty(binding.loginPassword.text.toString().trim()) -> {
                binding.loginPassword.setError("Please Enter the password", icon)
            }
            binding.loginUsername.text.toString().isNotEmpty() &&
                    binding.loginPassword.text.toString().isNotEmpty() -> {

                if (Patterns.EMAIL_ADDRESS.matcher(binding.loginUsername.text.toString()).matches()
                ) {
                    firebaseSignIn()
                    Toast.makeText(context, "Login Successful", Toast.LENGTH_LONG).show()
                } else {
                    binding.loginUsername.setError("Please Enter valid Email", icon)
                }
            }

        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity?)?.setDrawerUnlocked()
        _binding = null
    }

}