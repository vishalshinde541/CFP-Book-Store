package com.example.bookstore.view

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.bookstore.databinding.FragmentDialogProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*


class DialogProfileFragment : DialogFragment() {

    private var _binding : FragmentDialogProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var image: CircleImageView
    private lateinit var email: TextView
    private lateinit var fName: TextView
    private lateinit var clickToSaveImg: TextView
    private lateinit var profileImgBtn: CircleImageView
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var userId: String
    private lateinit var storage: FirebaseStorage
    private lateinit var selectedImg: Uri
    private lateinit var dialog: AlertDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDialogProfileBinding.inflate(inflater, container, false)
        val view = binding.root

        firebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        image = binding.dialogCircularProfilePic
        email = binding.dialogEmail
        fName = binding.dialogFname
        profileImgBtn = binding.dialogCircularProfilePic
        clickToSaveImg = binding.saveProfileImg

        dialog = AlertDialog.Builder(requireContext())
            .setMessage("Updating profile Image...")
            .setCancelable(false)

        readFirestoreData()

        clickToSaveImg.setOnClickListener {

            if (selectedImg!! == null) {
                Toast.makeText(requireContext(), "Please select your profile image", Toast.LENGTH_SHORT).show()
            } else {
                uploadImage()
            }
        }

        profileImgBtn.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }

        binding.dialogDismissBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                dismiss()
            }
        })



        return view
    }

    private fun readFirestoreData() {
        userId = firebaseAuth.currentUser?.uid!!
        val docRef = db.collection("user").document(userId)
        docRef.get()
            .addOnCompleteListener {

                if (it.isSuccessful) {
                    fName.text = it.result.getString("firstName")
                    email.text = it.result.getString("email")
                    val result = it.result.getString("imageUrl")
                    if (result != null){
                        Glide.with(this).load(it.result.getString("imageUrl")).into(image)
                    }
                } else {
                    Log.d(ContentValues.TAG, "No such document")
                }
            }
    }


    private fun uploadImage() {
        val reference = storage.reference.child("profile").child(Date().time.toString())
        reference.putFile(selectedImg).addOnCompleteListener {
            if (it.isSuccessful) {
                reference.downloadUrl.addOnSuccessListener { task ->
                    uploadInfo(task.toString())
                }
            }
        }
    }

    private fun uploadInfo(imgUrl: String) {
        userId = firebaseAuth.currentUser?.uid!!
        val docRef = db.collection("user").document(userId)
        docRef.update("imageUrl", imgUrl).addOnCompleteListener {
            Toast.makeText(requireContext(), "user profile image updated", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {
            if (data.data != null) {

                selectedImg = data.data!!
            }
            profileImgBtn.setImageURI(selectedImg)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity?)?.setDrawerUnlocked()
        _binding = null
    }

}