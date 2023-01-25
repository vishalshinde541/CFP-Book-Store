package com.example.bookstore.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.bookstore.R
import com.example.bookstore.databinding.FragmentUserNotLogedInDialogBinding


class UserNotLogedInDialogFragment : DialogFragment(R.layout.fragment_user_not_loged_in_dialog) {

    private var _binding : FragmentUserNotLogedInDialogBinding? = null
    private val binding get() = _binding!!

    private lateinit var dialog: AlertDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUserNotLogedInDialogBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.loginSignup.setOnClickListener {
            val fragment = LoginFragment()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragmentsContainer, fragment)?.commit()
            dismiss()
        }



        binding.dialogDismissBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                dismiss()
            }
        })


        return view

    }


}