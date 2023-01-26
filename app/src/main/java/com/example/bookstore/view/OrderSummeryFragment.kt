package com.example.bookstore.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.bookstore.R
import com.example.bookstore.databinding.FragmentOrderSummeryBinding

class OrderSummeryFragment : Fragment() {

    private var _binding : FragmentOrderSummeryBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentOrderSummeryBinding.inflate(inflater, container, false)
        val view = binding.root
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()



        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity?)?.setDrawerUnlocked()
        _binding = null
    }

}