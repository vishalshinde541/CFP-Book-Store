package com.example.bookstore.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.bookstore.R
import com.example.bookstore.databinding.FragmentOrderPlacedBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class OrderPlacedFragment : Fragment() {

    private var _binding: FragmentOrderPlacedBinding? = null
    private val binding get() = _binding!!

    private lateinit var bookId : String

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentOrderPlacedBinding.inflate(inflater, container, false)
        val view = binding.root

        firebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Receiving data(BookId) from adapter using Bundle
        val bundle = arguments
        if (bundle != null) {
            bookId = bundle.getString("BookId").toString()
        }

        binding.backBtn.setOnClickListener {
            val fragment = OrderSummeryFragment()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragmentsContainer, fragment)?.commit()
        }


        binding.ContinueShoppingBtn.setOnClickListener {

            db.collection("user").document(firebaseAuth.currentUser!!.uid)
                .collection("cartItems").document(bookId).delete()
                .addOnCompleteListener {
                    if (it.isSuccessful) {


                    } else {

                    }
                }

            val appCompatActivity = context as AppCompatActivity
            val fragment = HomeFragment()
            appCompatActivity.supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentsContainer, fragment)
                .addToBackStack(null)
                .commit()



        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}