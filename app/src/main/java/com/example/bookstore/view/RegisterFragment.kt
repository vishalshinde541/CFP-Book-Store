package com.example.bookstore.view

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.bookstore.R
import com.example.bookstore.databinding.FragmentRegisterBinding
import com.example.bookstore.mode.User
import com.example.bookstore.mode.UserAuthService
import com.example.bookstore.viewmodel.RegisterViewModel
import com.example.bookstore.viewmodel.RegisterViewModelFactory

class RegisterFragment : Fragment() {

    private lateinit var registerViewModel: RegisterViewModel
    private var _binding : FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerViewModel =
            ViewModelProvider(this, RegisterViewModelFactory(UserAuthService())).get(
                RegisterViewModel::class.java
            )

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val view = binding.root

       binding.tvLogin.setOnClickListener {
            val fragment = LoginFragment()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragmentsContainer, fragment)?.commit()
        }

       binding.registerBtn.setOnClickListener {
            validateEmptyField()
        }


        return view
    }

    private fun firbaseSignUp() {
        binding.registerBtn.isEnabled = false
        binding.registerBtn.alpha = 0.5f

        val user = User(
            firstName = binding.firstname.text.toString(),
            mobileNo = binding.moNumber.text.toString(),
            email = binding.etEmail.text.toString(),
            password = binding.setpassword.text.toString()



        )
        registerViewModel.registerUSer(user)
        registerViewModel._userRegisterStatus.observe(viewLifecycleOwner, Observer {

            if (it.status) {
                val fragment = HomeFragment()
                val transaction = fragmentManager?.beginTransaction()
                transaction?.replace(R.id.fragmentsContainer, fragment)?.commit()
            } else {
                binding.registerBtn.isEnabled = true
                binding.registerBtn.alpha = 1.0f
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun validateEmptyField() {
        val icon =
            AppCompatResources.getDrawable(requireContext(), R.drawable.ic_baseline_warning_24)
        icon?.setBounds(0, 0, icon.intrinsicWidth, icon.intrinsicHeight)

        when {
            TextUtils.isEmpty(binding.etEmail.text.toString().trim()) -> {
                binding.etEmail.setError("Please Enter the Email", icon)
            }
            TextUtils.isEmpty(binding.firstname.text.toString().trim()) -> {
                binding.firstname.setError("Please Enter firstname", icon)
            }
            TextUtils.isEmpty(binding.moNumber.text.toString().trim()) -> {
                binding.moNumber.setError("Please Enter mobile No.", icon)
            }
            TextUtils.isEmpty(binding.setpassword.text.toString().trim()) -> {
                binding.setpassword.setError("Please Enter the password", icon)
            }
            TextUtils.isEmpty(binding.confirmPassword.text.toString().trim()) -> {
                binding.confirmPassword.setError("Please confirm the password", icon)
            }

            binding.etEmail.text.toString().isNotEmpty() &&
                    binding.firstname.text.toString().isNotEmpty() &&
                    binding.moNumber.text.toString().isNotEmpty() &&
                    binding.setpassword.text.toString().isNotEmpty() &&
                    binding.confirmPassword.text.toString().isNotEmpty() -> {

                if (binding.etEmail.text.toString().matches(Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"))) {
                    if (binding.setpassword.text.toString().length >= 5) {
                        if (binding.setpassword.text.toString() == binding.confirmPassword.text.toString()) {
                            firbaseSignUp()
                            Toast.makeText(
                                requireContext(),
                                "Register Successful",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            binding.confirmPassword.setError("Password did't match", icon)
                        }
                    } else {
                        binding.setpassword.setError("please Enter at least 5 character", icon)
                    }
                } else {
                    binding.etEmail.setError("Please Enter valid Email", icon)
                }

            }

        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}