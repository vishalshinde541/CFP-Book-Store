package com.example.bookstore.mode

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserAuthService {

    private lateinit var database: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var userId: String

    init {
        initService()
    }

    private fun initService() {
        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
    }

    fun userRegister(user: User, listener: (AuthListener) -> Unit) {

        firebaseAuth.createUserWithEmailAndPassword(
            user.email,
            user.password
        ).addOnCompleteListener() {
            if (it.isSuccessful) {
                user.userId = firebaseAuth.currentUser?.uid.toString()
                saveUserDataToFirebase(user, listener)
                listener(AuthListener(true, "User Registered successfully"))
            } else {
                listener(AuthListener(false, "User not Registered"))
            }
        }

    }


    fun saveUserDataToFirebase(user: User, listener: (AuthListener) -> Unit) {

        val userMap = hashMapOf(
            "userId" to user.userId,
            "firstName" to user.firstName,
            "mobileNo" to user.mobileNo,
            "email" to user.email,
            "password" to user.password
        )

        var docref = database.collection("user").document(user.userId)
        docref.set(userMap).addOnCompleteListener {
            if (it.isSuccessful) {
                listener(AuthListener(true, "Data added successfully"))
            } else {
                listener(AuthListener(false, "Filed to add data"))
            }

        }
    }

    fun userLogin(user: User, listener: (AuthListener) -> Unit) {

        firebaseAuth.signInWithEmailAndPassword(
            user.email,
            user.password
        ).addOnCompleteListener() {
            if (it.isSuccessful) {
                listener(AuthListener(true, "User login successfully"))
            } else {
                listener(AuthListener(false, "User login filed"))
            }
        }

    }

    fun fetchUserInfo(user: User, listener: (UserAuthListener) -> Unit) {
        userId = firebaseAuth.currentUser?.uid!!
        val docRef = database.collection("user").document(userId)
        docRef.get().addOnCompleteListener {
            if (it.isSuccessful) {
                listener(UserAuthListener(true, "Data Fetch successfully", user))
            } else {
                listener(UserAuthListener(false, "Filed to fetch data", user))
            }

        }

    }
}