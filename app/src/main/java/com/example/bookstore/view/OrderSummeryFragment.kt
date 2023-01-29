package com.example.bookstore.view

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.bookstore.R
import com.example.bookstore.databinding.FragmentOrderSummeryBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import papaya.`in`.sendmail.SendMail

class OrderSummeryFragment : Fragment() {

    private var _binding : FragmentOrderSummeryBinding? = null
    private val binding get() = _binding!!
    private lateinit var bookId : String

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var userId: String
    private lateinit var fName: TextView
    private lateinit var email: TextView
    private lateinit var mobile: TextView
    private lateinit var addrees: TextView
    private lateinit var city: TextView
    private lateinit var state: TextView
    private lateinit var pincod: TextView
    private lateinit var bookImage: ImageView
    private lateinit var bookTitle: TextView
    private lateinit var author: TextView
    private lateinit var price: TextView

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

        firebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        userId = firebaseAuth.currentUser?.uid!!

        // Receiving data(BookId) using Bundle
        val bundle = arguments
        if (bundle != null) {
            bookId = bundle.getString("BookId").toString()
        }

        fName = binding.FnameOS
        email = binding.emailOS
        mobile = binding.mobileNoOS
        addrees = binding.addressOS
        city = binding.cityOS
        state = binding.stateOS
        pincod = binding.pincodeOS

        bookImage = binding.ivBookOS
        bookTitle = binding.tvBooktitleOS
        author = binding.tvAuthernameOS
        price = binding.tvPriceOS

        getBookDetailsFromFirebaseAndShowOnScreen()
        getShipingDetailsFromFirebaseAndShowOnScreen()

//        val mail = SendMail(
//            "shindevishal0311@gmail.com", "#Friendship#123",
//            "vishalshinde541@gmail.com",
//            "Your Order Placed Successfully",
//            "Congratulations Your order placed successfully"
//        )

        binding.placeOrderBtnOS.setOnClickListener {
//            mail.execute()
            val appCompatActivity = context as AppCompatActivity
            val fragment = OrderPlacedFragment()
            val bundle = Bundle()
            bundle.putString("BookId", bookId)
            fragment.arguments = bundle
            appCompatActivity.supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentsContainer, fragment)
                .addToBackStack(null)
                .commit()
        }

        binding.backBtn.setOnClickListener {
            val fragment = CustomerDetailsFragment()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragmentsContainer, fragment)?.commit()
        }


        return view
    }

    private fun getBookDetailsFromFirebaseAndShowOnScreen() {

        val docRef = db.collection("user").document(userId).collection("cartItems").document(bookId)
        docRef.get()
            .addOnCompleteListener {

                if (it.isSuccessful) {
                    Glide.with(this).load(it.result.getString("imageUrl")).into(bookImage)
                    bookTitle.text = it.result.getString("bookTitle")
                    author.text = it.result.getString("author")
                    price.text = it.result.getString("price")

                } else {
                    Log.d(ContentValues.TAG, "No such document")
                }
            }
    }

    private fun getShipingDetailsFromFirebaseAndShowOnScreen() {

        val addressID: String = "PermanentShippingAddress"

            val docRef = db.collection("user").document(userId).collection("userAddress").document(addressID)
            docRef.get()
                .addOnCompleteListener {

                    if (it.isSuccessful) {
                        fName.text = it.result.getString("FNAME")
                        email.text = it.result.getString("EMAIL")
                        mobile.text = it.result.getString("MOBILE")
                        addrees.text = it.result.getString("ADDRESS")
                        city.text = it.result.getString("CITY")
                        state.text = it.result.getString("STATE")
                        pincod.text = it.result.getString("PINCODE")


                    } else {
                        Log.d(ContentValues.TAG, "No such document")
                    }
                }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity?)?.setDrawerUnlocked()
        _binding = null
    }

}