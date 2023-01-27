package com.example.bookstore.view

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bookstore.R
import com.example.bookstore.databinding.FragmentCustomerDetailsBinding
import com.example.bookstore.mode.ShippingAdress
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CustomerDetailsFragment : Fragment() {

    private var _binding: FragmentCustomerDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var userId: String
    private lateinit var email: EditText
    private lateinit var fName: EditText
    private lateinit var mobile: EditText
    private lateinit var addrees: EditText
    private lateinit var city: EditText
    private lateinit var state: EditText
    private lateinit var pincod: EditText
    private lateinit var bookId : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        _binding = FragmentCustomerDetailsBinding.inflate(inflater, container, false)
        val view = binding.root
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
//        (activity as MainActivity?)?.setDrawerLocked()
        (activity as MainActivity).supportActionBar?.setTitle(R.string.customerdetail_title)


        // Receiving data(BookId) from adapter using Bundle
        val bundle = arguments
        if (bundle != null) {
            bookId = bundle.getString("BookId").toString()
        }

        firebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        fName = binding.ETNameShip
        email = binding.ETEmailShip
        mobile = binding.ETMobileNoShip
        addrees = binding.ETAddressShip
        city = binding.ETCityShip
        state = binding.ETStateShip
        pincod = binding.ETPincodeShip

        readFirestoreData()
        updateChangesInNote()


        binding.backBtn.setOnClickListener {
            val fragment = UserCartFragment()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragmentsContainer, fragment)?.commit()
        }

        return view
    }

    private fun updateChangesInNote() {

        binding.ContinueBtnShip.setOnClickListener {
            val addressID: String = "PermanentShippingAddress"
            val FNAME: String = email.text.toString()
            val EMAIL: String = fName.text.toString()
            val MOBILE: String = mobile.text.toString()
            val ADDRESS: String = addrees.text.toString()
            val CITY: String = city.text.toString()
            val STATE: String = state.text.toString()
            val PINCODE: String = pincod.text.toString()

            if (PINCODE.isEmpty() || ADDRESS.isEmpty() || CITY.isEmpty() || STATE.isEmpty()) {

                Toast.makeText(requireContext(), "Please fill all the details first", Toast.LENGTH_SHORT).show()

            } else{
                val shippingAdress =
                    ShippingAdress(addressID, FNAME, EMAIL, MOBILE, ADDRESS, CITY, STATE, PINCODE)
                val currentUserId = firebaseAuth.currentUser?.uid!!

                val shippingAddressMap = hashMapOf(
                    "IDadrees" to shippingAdress.IDaddress,
                    "FNAME" to shippingAdress.FNAME,
                    "EMAIL" to shippingAdress.EMAIL,
                    "MOBILE" to shippingAdress.MOBILE,
                    "ADDRESS" to shippingAdress.ADDRESS,
                    "CITY" to shippingAdress.CITY,
                    "STATE" to shippingAdress.STATE,
                    "PINCODE" to shippingAdress.PINCODE
                )

                db.collection("user").document(currentUserId).collection("userAddress")
                    .document(addressID).set(shippingAddressMap)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(requireContext(), "address updated successfully", Toast.LENGTH_SHORT).show()

                            val appCompatActivity = context as AppCompatActivity
                            val fragment = OrderSummeryFragment()
                            val bundle = Bundle()
                            bundle.putString("BookId", bookId)
                            fragment.arguments = bundle
                            appCompatActivity.supportFragmentManager.beginTransaction()
                                .replace(R.id.fragmentsContainer, fragment)
                                .addToBackStack(null)
                                .commit()

                        } else {
                            Toast.makeText(requireContext(), "Failed to update note", Toast.LENGTH_SHORT).show()
                        }

                    }

            }

        }
    }

    private fun readFirestoreData() {
        userId = firebaseAuth.currentUser?.uid!!
        val docRef = db.collection("user").document(userId)
        docRef.get()
            .addOnCompleteListener {

                if (it.isSuccessful) {
                    fName.setText(it.result.getString("firstName"))
                    email.setText(it.result.getString("email"))
                    mobile.setText(it.result.getString("mobileNo"))
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