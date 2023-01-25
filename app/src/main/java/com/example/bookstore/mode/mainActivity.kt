package com.example.bookstore.mode


import android.R
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    var minteger = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun increaseInteger(view: View?) {
        minteger = minteger + 1
        display(minteger)
    }

    fun decreaseInteger(view: View?) {
        minteger = minteger - 1
        display(minteger)
    }

    private fun display(number: Int) {
        val displayInteger = findViewById<View>(
            R.id.integer_number
        ) as TextView
        displayInteger.text = "" + number
    }
}