package com.example.bookstore.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.bookstore.R
import com.example.bookstore.databinding.FragmentUserCartBinding
import com.example.bookstore.mode.CartItemAdapter
import com.example.bookstore.mode.UserCart
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.ArrayList


class UserCartFragment : Fragment() {

    private var _binding : FragmentUserCartBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var db: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var bookList: ArrayList<UserCart>
    private lateinit var tempArrayList: ArrayList<UserCart>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUserCartBinding.inflate(inflater, container, false)
        val view = binding.root
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
        (activity as MainActivity).supportActionBar?.setTitle(R.string.cart_title)
        (activity as MainActivity?)?.setDrawerUnlocked()

        firebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        recyclerView = binding.recyclerCart
        recyclerView = view.findViewById(R.id.recycler_cart)

        bookList = arrayListOf<UserCart>()
        tempArrayList = arrayListOf<UserCart>()

        retrievBooksFromFirestoreAndStoreToBookList()


        return view
    }

    fun retrievBooksFromFirestoreAndStoreToBookList() {

        db.collection("user").document(firebaseAuth.currentUser?.uid!!).collection("cartItems")
            .get().addOnCompleteListener {

                if (it.isSuccessful) {
                    for (document in it.result) {
                        val cartBooks: UserCart = UserCart(
                            document["bookId"].toString(),
                            document["imageUrl"].toString(),
                            document["bookTitle"].toString(),
                            document["author"].toString(),
                            document["price"].toString()
                        )
                        bookList.add(cartBooks)

                    }


                    tempArrayList.addAll(bookList)

                    recyclerView.adapter = CartItemAdapter(requireContext(), tempArrayList)
                }

            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
            }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity?)?.setDrawerUnlocked()
        _binding = null
    }


}